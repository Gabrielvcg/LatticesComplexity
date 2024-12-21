package tests;

import java.util.List;
import java.util.function.Function;

import org.apache.commons.math3.fitting.WeightedObservedPoint;

import Algorithms.CVPNaive;
import Algorithms.SVPNaive;
import us.lsi.curvefitting.DataFile;
import us.lsi.curvefitting.Exponential;
import us.lsi.curvefitting.Fit;
import us.lsi.curvefitting.GenData;
import us.lsi.graphics.MatPlotLib;

public class TestLattices {

    private static Integer nMin = 1;  // Dimensión mínima
    private static Integer nMax = 3; // Dimensión máxima
    private static Integer nIncr = 1; // Incremento de dimensión
    private static Integer nIter = 50; // Número de iteraciones por medición
    private static Integer range = 5; // Rango de combinaciones [-k, k]
    private static Integer warmup= 1000; //calentamiento para saturar la cache
    
   
    public static void genDataSVP() {
        String file = "ficheros_generados/SVP.txt";

        // Función que mide el tiempo de ejecución
        Function<Integer, Long> f1 = dim -> {
            List<List<Integer>> base = SVPNaive.generateRandomBase(dim); // Base aleatoria
            List<List<Integer>> latticePoints = SVPNaive.generateLatticePoints(base, range);
            long startTime = System.nanoTime();
            SVPNaive.findShortestVector(latticePoints);
            long endTime = System.nanoTime();
    
            return endTime - startTime;
        };

        // Generar datos de tiempo para varias dimensiones
        GenData.tiemposEjecucionAritmetica(f1, file, nMin, nMax, nIncr, nIter, warmup);
    }

    public static void genDataNVP() {
        String file = "ficheros_generados/NVP.txt";

        // Función que mide el tiempo de ejecución
        Function<Integer, Long> f1 = dim -> {
            List<List<Integer>> base = CVPNaive.generateRandomBase(dim); // Base aleatoria
            List<List<Integer>> latticePoints = CVPNaive.generateLatticePoints(base, range);
            //El punto esta fuera del reticulo
            List<Integer> randomPoint = CVPNaive.generateRandomPoint(100, 100);
            long startTime = System.nanoTime();
            CVPNaive.findNearestVector(latticePoints, randomPoint);
            long endTime = System.nanoTime();
    
            return endTime - startTime;
        };
        // Generar datos de tiempo para varias dimensiones
        GenData.tiemposEjecucionAritmetica(f1, file, nMin, nMax, nIncr, nIter, warmup);
    }
        
    public static void showSVP() {
        String file = "ficheros_generados/SVP.txt";
        List<WeightedObservedPoint> data = DataFile.points(file);
        Fit pl = Exponential.of();
        pl.fit(data);
        System.out.println(pl.getExpression());
        System.out.println(pl.getEvaluation().getRMS());
        MatPlotLib.show(file, pl.getFunction(), pl.getExpression());
    }
    
    public static void showNVP() {
        String file = "ficheros_generados/NVP.txt";
        List<WeightedObservedPoint> data = DataFile.points(file);
        Fit pl = Exponential.of();
        pl.fit(data);
        System.out.println(pl.getExpression());
        System.out.println(pl.getEvaluation().getRMS());
        MatPlotLib.show(file, pl.getFunction(), pl.getExpression());
    }

    public static void showCombined() {
		MatPlotLib.showCombined("Tiempos",
				List.of("ficheros_generados/SVP.txt","ficheros_generados/NVP.txt"), 
				List.of("SVP","NVP"));

	}
    
    public static void main(String[] args) {
        genDataSVP();
        genDataNVP();
        showSVP();
        showNVP();
        showCombined();
    }
}