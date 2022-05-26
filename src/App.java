import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Random;
import com.google.gson.JsonSyntaxException;
import com.opencsv.CSVWriter;

public class App {
    public static void main(String[] args) throws Exception {

        ArrayList<Dados> entradas = ObterEntradasCSV();
        ArrayList<String[]> linhasDados = new ArrayList<>();

        for (int i = 0; i <= 29; i++) {
            Perceptron(entradas.get(i), i, linhasDados);
        }

        GravarDadosCSV(linhasDados);
    }

    public static void Perceptron(Dados entrada, int item, ArrayList<String[]> linhasDados) {
        Random aleatorio = new Random();
        float[] pesos = {
                (float) (aleatorio.nextFloat() * 1.000),
                (float) (aleatorio.nextFloat() * 1.000),
                (float) (aleatorio.nextFloat() * 1.000)
        };
        float[] pesosIniciais = pesos;
        float saida;
        int iteracoes = 0;
        while (true) {
            iteracoes++;
            saida = (entrada.x1 * pesos[0]) + (entrada.x2 * pesos[1]) + (entrada.x3 * pesos[2]);
            saida = saida >= 0 ? 1 : -1;

            if (saida == entrada.d) {
                linhasDados.add(new String[]{Float.toString(pesosIniciais[0]),
                    Float.toString(pesosIniciais[1]),
                    Float.toString(pesosIniciais[2]),
                    Float.toString(pesos[0]),
                    Float.toString(pesos[1]),
                    Float.toString(pesos[2]),
                    Float.toString(iteracoes)});
                break;
            } else
                pesos = ajustarPesos(entrada, pesos, saida, iteracoes);
        }
    }

    public static float[] ajustarPesos(Dados entrada, float[] pesosAnteriores, float saida, int iteracoes) {
        float taxaAprendizado = (float) 0.01;
        float[] pesoAtual = {
                (pesosAnteriores[0] + ((taxaAprendizado * (entrada.d - saida)) * entrada.x1)),
                (pesosAnteriores[1] + ((taxaAprendizado * (entrada.d - saida)) * entrada.x2)),
                (pesosAnteriores[2] + ((taxaAprendizado * (entrada.d - saida)) * entrada.x3))
        };

        return pesoAtual;
    }

    public static ArrayList<Dados> ObterEntradasCSV() throws JsonSyntaxException, IOException {

        FileInputStream fileInputStream = new FileInputStream("D:\\Repositorio\\Perceptron/Perceptron\\src\\dados.csv");
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(fileInputStream));
        ArrayList<Dados> entradasLista = new ArrayList<>();
        
        String strLine;
        Dados entrada = new Dados();
        String[] itensSeparados;
        while ((strLine = bufferedReader.readLine()) != null) {
            itensSeparados = strLine.split(";");
            entrada.x1 = Float.parseFloat(itensSeparados[0]);
            entrada.x2 = Float.parseFloat(itensSeparados[1]);
            entrada.x3 = Float.parseFloat(itensSeparados[2]);
            entrada.d = Float.parseFloat(itensSeparados[3]);

            entradasLista.add(entrada);
            entrada = new Dados();
        }
        return entradasLista;
    }

    public static void GravarDadosCSV(ArrayList<String[]> linhaDados) throws IOException {
        Writer writer = Files.newBufferedWriter(Paths.get("D:\\Repositorio\\Perceptron/Perceptron\\src\\dadosFinais.csv"));
        CSVWriter csvWriter = new CSVWriter(writer);
        String[] cabecalho = {"Peso inicial 1", "Peso inicial 2", "Peso inicial 3", "Peso final 1", "Peso finael 2", "Peso final 3", "Ajustes"};

        csvWriter.writeNext(cabecalho);
        csvWriter.writeAll(linhaDados);

        csvWriter.flush();
        writer.close();
    }
}
