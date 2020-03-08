import org.apache.poi.EncryptedDocumentException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.*;

public class JAVA {

    //korzystamy z zewnętrznych bibliotek org.apache.poi w celu zaimportowania danych z Excela

    public static void main(String[] a) throws EncryptedDocumentException, IOException {
        //pobieramy plik Excela
        FileInputStream fis = new FileInputStream("C:\\Users\\Dominik\\OneDrive\\UEP\\praca licencjacka\\praca_licencjacka.xlsx");
        Workbook wb = WorkbookFactory.create(fis);
        //wybieramy arkusz
        Sheet sheet = wb.getSheet("data-raw");
        //podajemy liczbę wierszy i kolumn
        int rows = 28;
        int columns = 10;
        //tworzymy pustą dwuwymiarową tablicę, którą zapełnimy danymi z pliku
        double[][] matrix = new double[28][10];
        //zapełniamy tablicę
        for (int i = 0; i < rows; i++) {
            Row row = sheet.getRow(i);
            for (int j = 0; j < columns; j++) {
                Cell cell = row.getCell(j);
                double cellVal = cell.getNumericCellValue();
                matrix[i][j] = cellVal;
            }
        }
        fis.close();
        //przypisujemy kryteriom dany typ: stymulanta lub destymulanta
        String[] types = new String[] {"stymulanta", "stymulanta", "stymulanta", "destymulanta", "stymulanta", "stymulanta", "stymulanta", "stymulanta", "stymulanta", "destymulanta"};
        //podajemy nazwy państw - badanych obiektów
        String[] names = new String[] {"Austria", "Belgia", "Bułgaria", "Chorwacja", "Cypr", "Czechy", "Dania", "Estonia", "Finlandia", "Francja", "Grecja", "Hiszpania", "Holandia", "Irlandia", "Litwa", "Luksemburg", "Łotwa", "Malta", "Niemcy", "Polska", "Portugalia", "Rumunia", "Słowacja", "Słowenia", "Szwecja", "Węgry", "Wielka Brytania", "Włochy"};
        //wywołujemy metodę getRanking z ww. parametrami zwracającą uszeregowany ranking
        getRanking(matrix, types, names);

    }

    static void getRanking(double[][] tab, String[] types, String[] objects) throws IllegalArgumentException {
        //sprawdzamy, czy wszystkie państwa realizują tyle samo kryteriów
        int width = tab[0].length;
        for (double[] row : tab) {
            //jeśli nie realizują, zwracamy błąd
            if (row.length != width) throw new IllegalArgumentException("Wszystkie wiersze muszą być tej samej długości");
        }
        //jeśli nie dla każdego kryterium podany jest typ (stymulanta/destymulanta), zwracamy błąd
        for (String s : types) {
            if (!s.equalsIgnoreCase("destymulanta") && !s.equalsIgnoreCase("stymulanta")) {
                throw new IllegalArgumentException("Wszystkie kryteria muszą być typu \"stymulanta\" lub \"destymulanta\"");
            }
        }
        if (width != types.length) throw new IllegalArgumentException("Każde kryterium musi mieć określony typ");
        //jeśli podajemy nazwy badanych obiektów, musimy podać ich odpowiednią liczbę
        if (objects != null) {
            if (objects.length != tab.length) throw new IllegalArgumentException("Wszystkie obiekty muszą mieć nazwy");
        }
        //mapę uzupełnimy wynikami
        Map<String, Double> result = new HashMap<>();
        for (int i = 0; i < width; i++) {
            //ustalamy minimum i maksimum dla każdego kryterium
            double min = tab[0][i];
            double max = tab[0][i];
            for (double[] j : tab) {
                if (j[i] <= min) min = j[i];
                if (j[i] >= max) max = j[i];
            }
            //korzystamy ze wzorów z podrozdziału 1.3.1.
            for (double[] j : tab) {
                if (types[i].equalsIgnoreCase("stymulanta")) {
                    j[i] = (j[i] - min)/(max - min);
                }
                else if (types[i].equalsIgnoreCase("destymulanta")) {
                    j[i] = (max - j[i])/(max - min);
                }
            }
        }
        //do mapy dopisujemy nazwy państw razem z sumą realizacji kryteriów przez nie
        for (int i = 0; i < tab.length; i++) {
            result.put(objects[i], sumInRow(tab[i]));
        }
        //sortujemy mapę malejąco
        LinkedHashMap<String, Double> reverse = new LinkedHashMap<>();
        result.entrySet().stream().sorted(Map.Entry.comparingByValue(Comparator.reverseOrder())).forEachOrdered(x -> reverse.put(x.getKey(), x.getValue()));
        //zwracamy ostateczny ranking
        int i = 1;
        for (String s : reverse.keySet()) {
            System.out.println(i++ + ". " + s + " " + reverse.get(s));
        }
    }

    static double sumInRow(double[] d) {
        //dla każdego wiersza wyznaczamy sumę realizacji kryteriów
        double sum = 0;
        for (double dd : d) sum += dd;
        return sum;
    }

}
