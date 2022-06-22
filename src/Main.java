import java.io.*;
import java.util.ArrayList;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class Main {
    private static ArrayList<String> listSave = new ArrayList<>();

    public static void main(String[] args) {
        // 1. Создать три экземпляра класса GameProgress.
        GameProgress save1 = new GameProgress(100, 10, 1, 30.0);
        GameProgress save2 = new GameProgress(90, 23, 5, 77.5);
        GameProgress save3 = new GameProgress(50, 15, 11, 123.6);

        // 2. Сохранить сериализованные объекты GameProgress в папку savegames из предыдущей задачи.
        saveGame("D://коля//JAVA//ДЗ//JavaCore//jd-homeworks-files-task1//Games//savegames", save1);
        saveGame("D://коля//JAVA//ДЗ//JavaCore//jd-homeworks-files-task1//Games//savegames", save2);
        saveGame("D://коля//JAVA//ДЗ//JavaCore//jd-homeworks-files-task1//Games//savegames", save3);

        // 3. Созданные файлы сохранений из папки savegames запаковать в архив zip
        zipFiles("D://коля//JAVA//ДЗ//JavaCore//jd-homeworks-files-task1//Games//savegames//zip.zip", listSave);

        // 4. Удалить файлы сохранений, лежащие вне архива.
        dellSave();
    }

    /*
    Метод принимающий полный путь до папки и объект для сохранения сериализации в указанную папку
    */
    public static void saveGame(String directory, GameProgress save) {
        String failName = directory + "//save" + (listSave.size() + 1) + ".txt";
        try (
                FileOutputStream fos = new FileOutputStream(failName);
                ObjectOutputStream oos = new ObjectOutputStream(fos)
        ) {
            oos.writeObject(save);
            listSave.add(failName);
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
    }

    // Метод записывающий созданные файлы сохранения в архив
    public static void zipFiles(String directory, ArrayList<String> listObjects) {
        // заводим счетчик для очередности имени
        int namberSave = 0;
        // накапливание для потока для записи в файил(ардес файла)
        try (FileOutputStream newFile = new FileOutputStream(directory);
             // создание архива (потока)
             ZipOutputStream zout = new ZipOutputStream(newFile)) {
            for (String a : listObjects) {
                // считывание данных из файла для архивирования в поток
                FileInputStream iputSteam = new FileInputStream(a);
                // увеличиваем счетчик на один
                namberSave++;
                // создание объекта который положим внутрь архива
                ZipEntry entry = new ZipEntry("save" + namberSave + ".txt");
                // передаем объект в архив
                zout.putNextEntry(entry);
                // создаем массив байт, размером = количеству байт в потоке
                byte[] buffer = new byte[iputSteam.available()];
                // записываем входящий поток в массив байт (побайтово)
                iputSteam.read(buffer);
                // записываем данные в поток создания zip
                zout.write(buffer);
                // закрываем поток создания zip
                zout.closeEntry();
                // закрываем поток file
                iputSteam.close();
            }
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
    }

    // Метод удаления файлов из папки
    public static void dellSave() {
        for (String filePath : listSave) {
            File currentFile = new File(filePath);
            if (currentFile.exists()) {
                currentFile.delete();
            } else {
                System.out.println("при удалении файла файл не найден");
            }
        }
    }
}
