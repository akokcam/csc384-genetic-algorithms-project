package ScheduleInstanceGenerator;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

import java.util.Random;
import java.util.Set;
import java.util.TreeSet;

/**
 * This is used to geenrate Scheduling problem instances with desired
 * properties. Just change the static final ints at the top.
 * @author dave
 */
public class Main {

    public static final String TESTINSTANCE = "Instance Input Files\\tickle.txt";
//    private static final String FILENAME = "Instance Input Files\\smallish instance.txt";
    public static final String FILENAME = TESTINSTANCE;
    // If this is set to true, we will generate an instance the size of the sun
    private static boolean useTrueNames = false;
    private static final String[] trueRoomNames = {"BN2N", "BN2S", "BN3", "BR200", "DEPT", "EAST", "EM119", "ES1050", "EX100", "EX200", "EX300", "EX310", "EX320", "HI-CART", "MP102", "MP103", "MP202", "MP203", "NF003", "NR25", "RW110", "RW117", "SEEL", "SHER", "SS1069", "SS1070", "SS1072", "SS1073", "SS1074", "SS1083", "SS1084", "SS1085", "SS1086", "SS2102", "SS2108", "SS2117", "SS2118", "SS2127", "SS2135", "STVLAD", "WEST", "WI1016", "WI1017"};
    private static final String[] trueCourseNames = {"ABS353H1F", "ACT230H1F", "ACT240H1F", "ACT348H1F", "ACT349H1F", "ACT451H1F", "ACT460H1F", "ACT460H1F", "ANA300Y1Y", "ANT204H1F", "ANT319Y1Y", "ANT345H1F", "ANT364Y1Y", "APM236H1F", "APM346H1F", "APM421H1F", "ARC131H1F", "ARC131H1F", "ARC335H1F", "AST101H1F", "AST101H1F", "AST101H1F", "AST101H1F", "AST101H1F", "AST101H1F", "AST101H1F", "AST101H1F", "AST210H1F", "BCH210H1F", "BCH210H1F", "BCH210H1F", "BCH210H1F", "BCH210H1F", "BCH210H1F", "BCH242Y1Y", "BCH335H1F", "BCH370H1F", "BCH371H1Y", "BCH422H1F", "BCH425H1F", "BCH441H1F", "BCH446H1F", "BCH471Y1Y", "BIO150Y1Y", "BIO240H1F", "BIO240H1F", "BIO240H1F", "BIO240H1F", "BIO240H1F", "BIO240H1F", "BIO240H1F", "BIO251Y1Y", "BIO255Y1Y", "BIO270H1F", "BIO270H1F", "CHM138H1F", "CHM139H1F", "CHM151Y1Y", "CHM217H1F", "CHM220H1F", "CHM220H1F", "CHM220H1F", "CHM247H1F", "CHM326H1F", "CHM338H1F", "CHM342H1F", "CHM347H1F", "CHM348H1F", "CHM410H1F", "CHM417H1F", "CHM427H1F", "CHM432H1F", "CHM434H1F", "CHM440H1F", "CHM441H1F", "CHM447H1F", "CLA160H1F", "CLA204H1F", "CLA219H1F", "CLA230H1F", "CLA230H1F", "CLA233H1F", "CLA362H1F", "CLA371H1F", "CLA382H1F", "CLA387H1F", "CSB325H1F", "CSB327H1F", "CSB328H1F", "CSB340H1F", "CSB343H1F", "CSB343H1F", "CSB428H1F", "CSB429H1F", "CSB435H1F", "CSB452H1F", "CSB460H1F", "CSC104H1F", "CSC108H1F", "CSC108H1F", "CSC108H1F", "CSC108H1F", "CSC148H1F", "CSC150H1F", "CSC165H1F", "CSC207H1F", "CSC207H1F", "CSC209H1F", "CSC236H1F", "CSC258H1F", "CSC263H1F", "CSC265H1F", "CSC290H1F", "CSC301H1F", "CSC310H1F", "CSC318H1F", "CSC324H1F", "CSC336H1F", "CSC343H1F", "CSC350H1F", "CSC363H1F", "CSC369H1F", "CSC373H1F", "CSC375H1F", "CSC411H1F", "CSC418H1F", "CSC438H1F", "CSC454H1F", "CSC458H1F", "CSC465H1F", "CSC487H1F", "EAS105H1F", "EAS120Y1Y", "EAS209Y1Y", "EAS220Y1Y", "EAS271H1F", "ECO210H1F", "ECO227Y1Y", "ECO230Y1Y", "ECO308H1F", "ECO313H1F", "ECO314H1F", "ECO320H1F", "ECO325H1F", "ECO326H1F", "ECO333Y1Y", "ECO336Y1F", "ECO342Y1Y", "ECO352H1F", "ECO358H1F", "ECO358H1F", "ECO364H1F", "ECO364H1F", "ECO364H1F", "ECO364H1F", "ECO364H1F", "ECO364H1F", "ECO374H1F", "ECO375H1F", "ECO380H1F", "ECO419H1F", "ECO433H1F", "EEB202H1F", "EEB215H1F", "EEB265Y1Y", "EEB318H1F", "EEB318H1F", "EEB321H1F", "EEB322H1F", "EEB323H1F", "EEB337H1F", "EEB362H1F", "EEB375H1F", "EEB389H1F", "EEB460H1F", "EEB465H1F", "EEB468H1F", "ENG100H1F", "ENG201Y1Y", "ENG214H1F", "ENG215H1F", "ENG232H1F", "ENG234H1F", "ENG236H1F", "ENG236H1F", "ENG285H1F", "ENG303H1F", "ENG305H1F", "ENG311H1F", "ENG323H1F", "ENG337H1F", "ENG348Y1Y", "ENG357H1F", "ENG383H1F", "ENV200Y1Y", "ENV222Y1Y", "ENV234Y1Y", "ENV236Y1Y", "ENV315H1F", "ENV320Y1Y", "ENV321Y1Y", "ENV346H1F", "ENV350H1F", "ENV410H1F", "ENV422H1F", "FAH101H1F", "FAH207H1F", "FAH207H1F", "FAH216H1F", "FAH230H1F", "FAH230H1F", "FAH246H1F", "FAH246H1F", "FAH272H1F", "FAH272H1F", "FAH331H1F", "FAH347H1F", "FAH347H1F", "FAH371H1F", "FOR200H1F", "FSL100H1F", "GGR101H1F", "GGR101H1F", "GGR107H1F", "GGR107H1F", "GGR107H1F", "GGR107H1F", "GGR107H1F", "GGR107H1F", "GGR124H1F", "GGR124H1F", "GGR124H1F", "GGR124H1F", "GGR124H1F", "GGR205H1F", "GGR206H1F", "GGR220H1F", "GGR222Y1Y", "GGR240H1F", "GGR240H1F", "GGR270H1F", "GGR270H1F", "GGR272H1F", "GGR272H1F", "GGR323H1F", "GGR327H1F", "GGR335H1F", "GGR337H1F", "GGR343H1F", "GGR352H1F", "GGR373H1F", "GGR374H1F", "GGR409H1F", "GLG103H1F", "GLG103H1F", "GLG105H1F", "GLG202H1F", "GLG206H1F", "GLG216H1F", "GLG318H1F", "GLG360H1F", "GLG442H1F", "GLG465H1F", "GRK100Y1Y", "GRK201H1F", "GRK341H1F", "GRK441H1F", "HIS208Y1Y", "HIS241H1F", "HIS241H1F", "HIS243H1F", "HIS243H1F", "HIS243H1F", "HIS250Y1Y", "HIS271Y1Y", "HIS312H1F", "HIS312H1F", "HIS314H1F", "HIS316H1F", "HIS322Y1Y", "HIS328H1F", "HIS338H1F", "HIS348H1F", "HIS358H1F", "HIS362H1F", "HIS365H1F", "HIS368H1F", "HIS368H1F", "HIS377H1F", "HIS387H1F", "HIS389H1F", "HMB201H1F", "HMB202H1F", "HMB202H1F", "HMB202H1F", "HMB203H1F", "HMB210H1F", "HMB301H1F", "HMB302H1F", "HMB302H1F", "HMB302H1F", "HMB302H1F", "HMB304H1F", "HMB306H1F", "HMB325H1F", "HMB402H1F", "HMB422H1F", "HMB430H1F", "HMB434H1F", "HMB435H1F", "HMB436H1F", "HMB441H1F", "HMB472H1F", "HMB473H1F", "HPS100H1F", "HPS100H1F", "HPS100H1F", "HPS210H1F", "IFP100Y1Y", "IMM429H1F", "IMM435H1F", "INI387H1F", "ITA100Y1Y", "ITA101Y1Y", "ITA343H1F", "ITA370H1F", "JAL328H1F", "JBI428H1F", "JBO302Y1Y", "JEH455H1F", "JGE331H1F", "JGE347H1F", "JGI346H1F", "JGI454H1F", "JGP438H1F", "JHE353H1F", "JLP374H1F", "JLS474H1F", "JPR364Y1Y", "LAT100Y1Y", "LAT201H1F", "LAT341H1F", "LAT351H1F", "LAT430H1F", "LAT441H1F", "LAT451H1F", "LIN100Y1Y", "LIN200H1F", "LIN200H1F", "LIN201H1F", "LIN203H1F", "LIN228H1F", "LIN232H1F", "LIN362H1F", "LMP363H1F", "LMP402H1F", "LMP404H1F", "LMP410H1F", "LMP412H1F", "MAT223H1F", "MAT223H1F", "MAT223H1F", "MAT223H1F", "MAT223H1F", "MAT224H1F", "MAT240H1F", "MAT244H1F", "MAT246H1F", "MAT267H1F", "MAT271H1F", "MAT301H1F", "MAT309H1F", "MAT327H1F", "MAT332H1F", "MAT334H1F", "MAT335H1F", "MAT354H1F", "MAT409H1F", "MAT464H1F", "MAT475H1F", "MGY377H1F", "MGY377H1F", "MGY377H1F", "MGY420H1F", "MGY428H1F", "MGY432H1F", "MGY440H1F", "MGY445H1F", "MGY451H1F", "MUS111H1F", "MUS200H1F", "MUS202H1F", "NEW232Y1Y", "NEW240Y1Y", "NEW333H1F", "NFS284H1F", "NFS284H1F", "NFS386H1F", "NFS484H1F", "NFS489H1F", "NMC184H1F", "NMC255H1F", "NMC278H1F", "NMC343H1F", "NMC366Y1F", "NML155H1F", "PCL302H1F", "PCL376H1F", "PCL389H1F", "PCL477H1F", "PHC330Y1Y", "PHE110H1F", "PHL100Y1Y", "PHL205H1F", "PHL205H1F", "PHL205H1F", "PHL205H1F", "PHL235H1F", "PHL240H1F", "PHL240H1F", "PHL244H1F", "PHL265H1F", "PHL275H1F", "PHL281H1F", "PHL281H1F", "PHL308H1F", "PHL310H1F", "PHL337H1F", "PHL340H1F", "PHL341H1F", "PHL342H1F", "PHL351H1F", "PHL365H1F", "PHL365H1F", "PHL375H1F", "PHL378H1F", "PHL380H1F", "PHL404H1F", "PHY100H1F", "PHY100H1F", "PHY131H1F", "PHY131H1F", "PHY131H1F", "PHY131H1F", "PHY131H1F", "PHY131H1F", "PHY132H1F", "PHY151H1F", "PHY201H1F", "PHY201H1F", "PHY231H1F", "PHY252H1F", "PHY254H1F", "PHY350H1F", "PHY356H1F", "PHY456H1F", "PHY485H1F", "PHY492H1F", "POL103Y1Y", "POL300H1F", "POL315H1F", "POL345H1F", "POL372H1F", "POL381H1F", "PSL300H1F", "PSL302Y1Y", "PSL303Y1Y", "PSL420H1F", "PSL440Y1Y", "PSL443H1F", "PSL444Y1Y", "PSL450H1F", "PSL452H1F", "PSY100H1F", "PSY100H1F", "PSY100H1F", "PSY100H1F", "PSY100H1F", "PSY100H1F", "PSY100H1F", "PSY201H1F", "PSY201H1F", "PSY210H1F", "PSY220H1F", "PSY220H1F", "PSY240H1F", "PSY240H1F", "PSY260H1F", "PSY270H1F", "PSY280H1F", "PSY280H1F", "PSY290H1F", "PSY290H1F", "PSY311H1F", "PSY320H1F", "PSY328H1F", "PSY330H1F", "PSY332H1F", "PSY341H1F", "PSY343H1F", "PSY370H1F", "RLG249H1F", "RLG350H1F", "RSM100Y1Y", "RSM220H1F", "RSM220H1F", "RSM220H1F", "RSM221H1F", "RSM222H1F", "RSM225H1F", "RSM230H1F", "RSM230H1F", "RSM230H1F", "RSM250H1F", "RSM260H1F", "RSM260H1F", "RSM320H1F", "RSM320H1F", "RSM321H1F", "RSM323H1F", "RSM324H1F", "RSM324H1F", "RSM327H1F", "RSM330H1F", "RSM332H1F", "RSM332H1F", "RSM333H1F", "RSM360H1F", "RSM392H1F", "RSM410H1F", "RSM412H1F", "RSM415H1F", "RSM418H1F", "RSM422H1F", "RSM423H1F", "RSM430H1F", "RSM430H1F", "RSM435H1F", "RSM435H1F", "RSM451H1F", "RSM460H1F", "RSM470H1F", "RSM480H1F", "RSM481H1F", "RSM490H1F", "SCI199H1F", "SLA110H1F", "SLA228H1F", "SLA314H1F", "SMC103Y1Y", "SMC201H1F", "SMC203Y1Y", "SMC216Y1Y", "SMC222H1F", "SMC228H1F", "SMC228H1F", "SMC308H1F", "SMC313H1F", "SMC330Y1Y", "SMC361H1F", "SOC243H1F", "SOC263H1F", "SOC263H1F", "SOC364H1F", "SPA258H1F", "STA220H1F", "STA220H1F", "STA220H1F", "STA247H1F", "STA247H1F", "STA250H1F", "STA257H1F", "STA257H1F", "STA302H1F", "STA302H1F", "STA304H1F", "STA347H1F", "STA437H1F", "STA442H1F", "UNI255H1F", "UNI302H1F", "UNI307H1F", "UNI377H1F", "VIC109H1F", "VIC115H1F", "VIC118H1F", "VIC131H1F", "VIC163H1F", "VIC181H1F", "VIC184H1F", "VIC185H1F", "VIS120H1F", "VIS120H1F", "WDW205H1F", "WDW260H1F", "WDW300H1F", "WDW335H1F", "WDW346H1F", "WDW348H1F", "WDW370H1F", "WDW378H1F", "WDW383H1F", "WDW430Y1Y"};
    private static final int NUMDAYS = 5;
    private static final int NUMTIMES = 5;
    private static final int NUMSTUDENTS = 50;
    private static final int MAXCOURSESPERSTUDENT = 7;
    private static final int NUMROOMS = 4;
    private static final int NUMCOURSES = 25;

    public static void main(String[] args) {
        Random rand = new Random();

        try {
            FileWriter fstream = new FileWriter(FILENAME);
            BufferedWriter out = new BufferedWriter(fstream);
//            writeStream out = System.out;
            out.write(NUMDAYS + " " + NUMTIMES + "\n");

            // Output the courses and their names
            if (useTrueNames) {
                out.write("\n" + trueCourseNames.length + "\n");
                for (int c = 0; c < trueCourseNames.length; c++) {
                    out.write(trueCourseNames[c] + "\n");
                }
            } else {
                out.write("\n" + NUMCOURSES + "\n");
                for (int c = 1; c <= NUMCOURSES; c++) {
                    out.write("Course " + c + "\n");
                }
            }


            // Output the rooms and their names
            if (useTrueNames) {
                out.write("\n" + trueRoomNames.length + "\n");
                for (int r = 0; r < trueRoomNames.length; r++) {
                    out.write(trueRoomNames[r] + "\n");
                }
            } else {
                out.write("\n" + NUMROOMS + "\n");
                for (int r = 1; r <= NUMROOMS; r++) {
                    out.write("Room " + r + "\n");
                }
            }

            // Output the students
            out.write("\n" + NUMSTUDENTS + "\n");
            for (int i = 1; i <= NUMSTUDENTS; i++) {
                out.write("Student " + i + "\n");
                // Tend toward more courses by taking the sqrt of a rand float under 1
                int numCourses = (int) (Math.sqrt(rand.nextFloat()) * MAXCOURSESPERSTUDENT);
                if (numCourses == 0) {
                    numCourses = 1;
                }
                out.write(numCourses + "\n");
                Set cs = new TreeSet();
                for (int k = 0; k < numCourses; k++) {
                    int coursenum = 0;
                    // For simplicity when we're using the true names or not
                    coursenum = (int) (useTrueNames ? rand.nextFloat() * trueCourseNames.length : 1 + rand.nextFloat() * NUMCOURSES);
                    if (cs.contains(coursenum)) {
                        k--;
                    } else {
                        // For simplicity when we're using the true names or not
                        out.write(useTrueNames ? trueCourseNames[coursenum] + "\n" : "Course " + coursenum + "\n");
                        cs.add(coursenum);
                    }

                }
            }


            out.close();
        } catch (IOException e) {
        }
    }
}
