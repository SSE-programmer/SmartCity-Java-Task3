public class TestScannerIO {
    public static void main(String[] args) {

        /*Создадим первый сканер*/
        FlatbedScanner scannerHp = new FlatbedScanner
                .Builder("HP", 999.9f)
                .setGrayscale(3)
                .setOpticalResolution(1200 * 600)
                .setScanAreaHeight(210)
                .setScanAreaWidth(297)
                .build();
        System.out.println(scannerHp);

        /*Сразу попробуем загрузить в него данные из файла
        (не получится, если файла нет, или он некорректного формата*/
        scannerHp.load("myScanners.bin", 2);

        /*Создадим второй сканер*/
        FlatbedScanner scannerCanon = new FlatbedScanner.Builder("Canon", 500).build();
        System.out.println();
        System.out.println(scannerCanon);

        /*сохраним оба сканера в файл*/
        scannerHp.save("myScanners.bin");
        scannerCanon.save("myScanners.bin");

        /*Создадим третий сканер*/
        FlatbedScanner newScanner = new FlatbedScanner.Builder().build();

        System.out.println();
        System.out.println(newScanner);

        /*Загрузим свойства для третьего сканера из файла, параметром передадим номер 1
        Это должен быть принтер HP с точно такими же свойствами*/
        newScanner.load("myScanners.bin", 1);

        System.out.println();
        System.out.println(newScanner);

        /*Работает =) */

        /*Было бы неплохо добавить загрузку из файла с параметром для поиска не номером, а именем*/
        /*И можно добавить удаление записей из файла по номеру или имени:
            доходим до нужной области, удаляем ее, то что осталось впереди смещаем назад на удаленное кол-во байт
        */
    }
}
