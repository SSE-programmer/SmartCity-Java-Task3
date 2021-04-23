/*Попробовал реализовать паттерн Builder */

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;

public class FlatbedScanner {
    private String name;
    private double price;           // $$$
    private int opticalResolution;  // dpi
    private int grayscale;          // bytes
    private int scanAreaWidth;      //mm
    private int scanAreaHeight;     //mm

    private FlatbedScanner(Builder builder) {
        this.name = builder.name;
        this.price = builder.price;
        this.opticalResolution = builder.opticalResolution;
        this.grayscale = builder.grayscale;
        this.scanAreaWidth = builder.scanAreaWidth;
        this.scanAreaHeight = builder.scanAreaHeight;
    }

    public String getName() {
        return name;
    }

    public double getPrice() {
        return price;
    }

    public int getOpticalResolution() {
        return opticalResolution;
    }

    public int getGrayscale() {
        return grayscale;
    }

    public int getScanAreaWidth() {
        return scanAreaWidth;
    }

    public int getScanAreaHeight() {
        return scanAreaHeight;
    }

    static class Builder {
        private String name = "nameless";
        private double price = 100.0;           // $$$
        private int opticalResolution = 1200 * 1200;  // dpi
        private int grayscale = 3;          // bytes
        private int scanAreaWidth = 297;      //mm
        private int scanAreaHeight = 210;     //mm

        public Builder() {}

        public Builder(String name, double price) {
            this.name = name;
            this.price = price;
        }

        public Builder setOpticalResolution(int opticalResolution) {
            this.opticalResolution = opticalResolution;
            return this;
        }

        public Builder setGrayscale(int grayscale) {
            this.grayscale = grayscale;
            return this;
        }

        public Builder setScanAreaWidth(int scanAreaWidth) {
            this.scanAreaWidth = scanAreaWidth;
            return this;
        }

        public Builder setScanAreaHeight(int scanAreaHeight) {
            this.scanAreaHeight = scanAreaHeight;
            return this;
        }

        public FlatbedScanner build() {
            return new FlatbedScanner(this);
        }
    }

    public int save(String fileName) {
        try {
            File file = new File(fileName);

            if(!file.exists()) {
                file.createNewFile();
            }

            RandomAccessFile f = new RandomAccessFile(file,"rw");
            long count;

            if (f.length() >= 8) {
                count = f.readLong();
            } else {
                count = -1;
            }

            f.seek(0);

            if (count == -1) {
                f.writeLong(1L);
            } else {
                f.writeLong(++count);
                f.seek(f.length());
            }

            f.writeInt(name.length());
            f.writeChars(name);
            f.writeDouble(price);
            f.writeInt(opticalResolution);
            f.writeInt(grayscale);
            f.writeInt(scanAreaWidth);
            f.writeInt(scanAreaHeight);

            f.close();
        } catch (FileNotFoundException e) {
            System.out.println("Не удалось найти и создать файл " + fileName + ".");

            return -1;
        } catch (IOException e) {
            System.out.println("Ошибка при работе с файлом " + fileName +".");
            e.printStackTrace();

            return -1;
        }

        return 0;
    }

    public int load(String fileName, int number) {
        try {
            File file = new File(fileName);

            RandomAccessFile f = new RandomAccessFile(file,"r");

            long count;

            if (f.length() >= 8) {
                count = f.readLong();
            } else {
                System.out.println("Невозможно считать данные сканера с этого файла. " +
                        "Файл содержит неккоректные данные, либо пуст.");

                return -1;
            }

            if (number > count || number < 1) {
                System.out.println("Введен некорректный номер сканера. Всего в базе: " + count);

                return -1;
            }

            if (count < 1) {
                System.out.println("Невозможно считать данные сканера с этого файла. " +
                        "Файл содержит неккоректные данные, либо пуст.");
            }

            for (int n = 0; n < number; n++) {
                int lenName = f.readInt();
                char[] tempName = new char[lenName];

                for (int i = 0; i < lenName; i++) {
                   tempName[i] = f.readChar();
                }

                name = new String(tempName);
                price = f.readDouble();
                opticalResolution = f.readInt();
                grayscale = f.readInt();
                scanAreaWidth = f.readInt();
                scanAreaHeight = f.readInt();
            }

            f.close();
        } catch (FileNotFoundException e) {
            System.out.println("Не удалось найти файл " + fileName + ".");

            return -1;
        } catch (IOException e) {
            System.out.println("Ошибка при работе с файлом " + fileName +".");
            e.printStackTrace();

            return -1;
        }

        return 0;
    }

    @Override
    public String toString() {
        return "FlatbedScanner{\n" +
                "\tname = '" + name + '\'' + ",\n" +
                "\tprice = " + Math.round(price * 100) / 100.0 + "$,\n" +
                "\topticalResolution = " + opticalResolution + " dpi,\n" +
                "\tgrayscale = " + grayscale + " byte,\n" +
                "\tscanAreaWidth = " + scanAreaWidth + " mm,\n" +
                "\tscanAreaHeight = " + scanAreaHeight + " mm,\n" +
                '}';
    }
}
