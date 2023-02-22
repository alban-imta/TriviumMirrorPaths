import org.chocosolver.solver.Model;
import org.chocosolver.solver.variables.IntVar;

import java.util.ArrayList;
import java.util.Arrays;

import static org.chocosolver.solver.search.strategy.Search.inputOrderLBSearch;


public class triviumDoublePathFinder {
    public static void postState(Model model, IntVar val, IntVar etat1, IntVar etat2){
        model.ifThen(model.arithm(val,"=",111), model.and(model.arithm(etat1,"=",1),model.arithm(etat2,"=",3)));
        model.ifThen(model.arithm(val,"=",110), model.and(model.arithm(etat1,"=",1),model.arithm(etat2,"=",3)));
        model.ifThen(model.arithm(val,"=",109), model.and(model.arithm(etat1,"=",1),model.arithm(etat2,"=",3)));
        //model.ifThen(model.arithm(val,"=",66), model.and(model.arithm(etat1,"=",1),model.arithm(etat2,"=",3)));
        //model.ifThen(model.arithm(val,"=",69), model.and(model.arithm(etat1,"=",1),model.arithm(etat2,"=",1)));

        model.ifThen(model.arithm(val,"=",82), model.and(model.arithm(etat1,"=",3),model.arithm(etat2,"=",2)));
        model.ifThen(model.arithm(val,"=",83), model.and(model.arithm(etat1,"=",3),model.arithm(etat2,"=",2)));
        model.ifThen(model.arithm(val,"=",84), model.and(model.arithm(etat1,"=",3),model.arithm(etat2,"=",2)));
        //model.ifThen(model.arithm(val,"=",69), model.and(model.arithm(etat1,"=",3),model.arithm(etat2,"=",2)));
        model.ifThen(model.arithm(val,"=",87), model.and(model.arithm(etat1,"=",3),model.arithm(etat2,"=",3)));

        model.ifThen(model.arithm(val,"=",91), model.and(model.arithm(etat1,"=",2),model.arithm(etat2,"=",1)));
        model.ifThen(model.arithm(val,"=",92), model.and(model.arithm(etat1,"=",2),model.arithm(etat2,"=",1)));
        model.ifThen(model.arithm(val,"=",93), model.and(model.arithm(etat1,"=",2),model.arithm(etat2,"=",1)));
        //model.ifThen(model.arithm(val,"=",66), model.and(model.arithm(etat1,"=",2),model.arithm(etat2,"=",1)));
        model.ifThen(model.arithm(val,"=",78), model.and(model.arithm(etat1,"=",2),model.arithm(etat2,"=",2)));

        model.ifThen(model.arithm(val,"=",66), model.arithm(etat1,"!=",3));
        model.ifThen(model.arithm(val,"=",66), model.arithm(etat1,"!=",4));
        model.ifThen(model.and(model.arithm(val,"=",66),model.arithm(etat1,"=",2)),model.arithm(etat2,"=",1));
        model.ifThen(model.and(model.arithm(val,"=",66),model.arithm(etat1,"=",1)),model.arithm(etat2,"=",3));
        model.ifThen(model.arithm(val,"=",69), model.arithm(etat1,"!=",2));
        model.ifThen(model.arithm(val,"=",69), model.arithm(etat1,"!=",4));
        model.ifThen(model.and(model.arithm(val,"=",69),model.arithm(etat1,"=",1)),model.arithm(etat2,"=",1));
        model.ifThen(model.and(model.arithm(val,"=",69),model.arithm(etat1,"=",3)),model.arithm(etat2,"=",2));

        //on ajoute un puit.
        model.ifThen(model.arithm(val,"=",0), model.arithm(etat2,"=",4));
        model.ifThen(model.arithm(etat1,"=",4), model.arithm(etat2,"=",4));
    }
    public static void exportToCmdline(int min, int max){
        for(int valToFind = min;valToFind<=max; valToFind++){
            System.out.println("Finding solution of a path of length: "+valToFind);
            Model model = new Model("Choco Solver Trivium Paths mirrored");
            // Integer variables
            IntVar []valeur = model.intVarArray(10,new int[]{0,66, 111,69, /*110, 109,*/ 78, 66, 93, /*92, 91,*/87, 69, 84, /*83, 82*/});
            IntVar []etat = model.intVarArray(10+1,new int[]{1,2,3,4});

            for(int i=0;i< valeur.length;i++){
                postState(model,valeur[i],etat[i],etat[i+1]);
            }
            model.sum(valeur,"=",valToFind).post();
            ArrayList<IntVar> al = new ArrayList<>(Arrays.stream(etat).toList());
            al.addAll(Arrays.stream(valeur).toList());

            //set a search to order solution (easier to check equal path by hand afterward)
            model.getSolver().setSearch(inputOrderLBSearch(al.toArray(new IntVar[0])));

            int i = 1;
            // Computes all solutions (works because Solver.solve() returns true whenever a new feasible solution has been found)
            while (model.getSolver().solve()) {
                System.out.print(valToFind+" solution " + i++ + " found : ");
                printSolution(valeur,etat);
            }
        }
    }

    public static void exportToLatex(int min, int max){
        for(int i=1;i<4;i++){
            //System.out.println("private void buildPathto"+(char)(i+64)+"() {");
            for(int j=1;j<4;j++){
                System.out.println("\\begin{figure}[H]\\centering\n\\begin{tabular}{|c|l|} \\hline\n     Path Length & Paths  \\\\\\hline");
                printPathLatex(min,max,15,i,j);
                System.out.println("\\end{tabular}\n    \\caption{List of path from register "+(char)(i+64)+" to register "+(char)(j+64)+"}\n    \\label{fig:my_label"+(char)(i+64)+"to"+(char)(j+64)+"}\n\\end{figure}\n");
            }
        }
    }
    public static void printPathLatex(int min, int max, int nbTransition, int etatFirst, int etatLast){
        for(int valToFind = min;valToFind<=max; valToFind++) {
            StringBuilder sb = new StringBuilder();
            sb.append(valToFind+"&");


            Model model = new Model("Choco Solver Trivium path finder");
            // Integer variables
            IntVar []valeur = model.intVarArray(nbTransition,new int[]{0,66, 111,69, /*110, 109,*/ 78, 66, 93, /*92, 91,*/87, 69, 84, /*83, 82*/});
            IntVar []etat = model.intVarArray(nbTransition+1,new int[]{1,2,3,4});
            model.arithm(etat[0] ,"=",etatFirst).post();

            for(int i=0;i< valeur.length;i++){
                postState(model,valeur[i],etat[i],etat[i+1]);
                //enforce etat final fixed
                for(int j=1;j<4;j++){
                    if (j!=etatLast)  model.ifThen(model.arithm(valeur[i],"=",0), model.arithm(etat[i],"!=",j));
                }
            }
            model.sum(valeur,"=",valToFind).post();


            //set a search to order solutions (easier to check equal path by hand afterward)
            ArrayList<IntVar> al = new ArrayList<>(Arrays.stream(etat).toList());
            al.addAll(Arrays.stream(valeur).toList());
            model.getSolver().setSearch(inputOrderLBSearch(al.toArray(new IntVar[0])));

            // Computes all solutions
            int cptSol = 0; // count the number of solutions
            while (model.getSolver().solve()) {
                cptSol++;
                for(int j=0;j<etat.length;j++){
                    if(etat[j+1].getValue()==4) {
                        break;
                    }
                    sb.append(printEtat(etat[j])+", "+valeur[j].getValue()+", ");
                }
                sb.append(printEtat(etatLast)+", ");
                if(sb.length()>2) sb.setLength(sb.length()-2);
                sb.append("\\\\\n   & ");
            }
            if(sb.length()>9) sb.setLength(sb.length()-8);
            sb.append(" \\\\ \\hline\n");
            if(cptSol>1){
                System.out.print(sb);
            }
        }
    }



    public static void exportToJava(int min, int max){
        for(int i=1;i<4;i++){
            System.out.println("private void buildPathto"+(char)(i+64)+"() {");
            System.out.println("\tmirroredPath = new int [NBREGISTER][MAXPATHLENGTH][][];");
            for(int j=1;j<4;j++){
                printPathComplete(min,max,15,j,i);
            }
            System.out.println("}");
        }
    }
    public static void printPathComplete(int min, int max, int nbTransition, int etatFirst, int etatLast){
        for(int valToFind = min;valToFind<=max; valToFind++){
            StringBuilder sb = new StringBuilder();
            sb.append("\tmirroredPath[").append(etatFirst-1).append("][").append(valToFind).append("] = new int[][]{");
            //mirroredPath[1][321] = new int[][]{ {     93-triv.nbMaxNodePerRegistre, 66+2*triv.nbMaxNodePerRegistre, 84-triv.nbMaxNodePerRegistre,78 },
            //                                            { 78, 93-triv.nbMaxNodePerRegistre, 66+2*triv.nbMaxNodePerRegistre, 84-triv.nbMaxNodePerRegistre }};
            //System.out.println("Tring to find solution of a path of length: "+valToFind);
            Model model = new Model("Choco Solver ");
            // Integer variables
            IntVar []valeur = model.intVarArray(nbTransition,new int[]{0,66, 111,69, /*110, 109,*/ 78, 66, 93, /*92, 91,*/87, 69, 84, /*83, 82*/});
            IntVar []etat = model.intVarArray(nbTransition+1,new int[]{1,2,3,4});
            model.arithm(etat[0],"=",etatFirst).post();//enforce etat 1 fixed.

            for(int i=0;i< valeur.length;i++){
                postState(model,valeur[i],etat[i],etat[i+1]);
                //enforce etat final fixed
                for(int j=1;j<4;j++){
                    if (j!=etatLast)  model.ifThen(model.arithm(valeur[i],"=",0), model.arithm(etat[i],"!=",j));
                }
            }
            model.sum(valeur,"=",valToFind).post();


            //set a search to order solution (easier to check equal path by hand afterward)
            ArrayList<IntVar> al = new ArrayList<>(Arrays.stream(etat).toList());
            al.addAll(Arrays.stream(valeur).toList());
            model.getSolver().setSearch(inputOrderLBSearch(al.toArray(new IntVar[0])));

            int i = 0;
            // Computes all solutions (works because Solver.solve() returns true whenever a new feasible solution has been found)
            while (model.getSolver().solve()) {
                i++;
                sb.append('{');
                for(int j=0;j<etat.length;j++){
                    if(etat[j+1].getValue()==4) {
                        break;
                    }
                    appendDistanceDirect(sb,valeur[j].getValue(),etat[j+1].getValue()-etat[j].getValue());
                }
                if(sb.length()>2) sb.setLength(sb.length()-2);
                sb.append("}, ");
            }
            if(sb.length()>2) sb.setLength(sb.length()-2);
            sb.append("};");
            if(i>1){
                System.out.println(sb);
            }
            //model.getSolver().printShortStatistics();
        }
    }
    public static void appendDistance(StringBuilder sb, int valeur, int deltaEtat){
        sb.append(valeur);
        if(deltaEtat==-1){
            sb.append("-triv.nbMaxNodePerRegistre, ");
        }else if(deltaEtat==0){
            sb.append(", ");
        }else if(deltaEtat==2){
            sb.append("+2*triv.nbMaxNodePerRegistre, ");
        }
    }
    public static void appendDistanceDirect(StringBuilder sb, int valeur, int deltaEtat){
        int trivNBmaxPerRegiter = 1000;
        sb.append(valeur+deltaEtat*trivNBmaxPerRegiter).append(", ");
    }




    public static void printSolution(IntVar[] valeur,IntVar[] etat){
        int nbTransition= valeur.length;
        for(int j=0;j<nbTransition;j++){
            System.out.print(printEtat(etat[j])+ " ");
            if(valeur[j].isInstantiatedTo(0)) continue;
            System.out.print(valeur[j].getValue());
        }
        System.out.print(printEtat(etat[nbTransition]));
        System.out.println();
    }
    public static String printEtat(IntVar etat){
        String res =" ";
        if (etat.isInstantiatedTo(4)) return res;
        if (etat.isInstantiatedTo(1)) res+="A";
        if (etat.isInstantiatedTo(2)) res+="B";
        if (etat.isInstantiatedTo(3)) res+="C";
        return res;
    }
    public static String printEtat(int etat){
        String res =" ";
        if (etat == (4)) return res;
        if (etat==(1)) res+="A";
        if (etat==(2)) res+="B";
        if (etat==(3)) res+="C";
        return res;
    }
}
