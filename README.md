# DataSorterShift

Утилита для фильтрации данных из нескольких входных файлов по типу (целые числа, вещественные, строки) и сбора по ним статистики.

---

## Требования

- **Java**: 17  
- **Система сборки**: Maven 3.14.0  
  - плагин `maven-compiler-plugin` версии 3.14.0  
  - плагин `maven-resources-plugin` версии 3.3.1  
  - плагин `maven-jar-plugin` версии 3.4.2  


---

## Сборка

1. Клонировать репозиторий:
   ```bash
   git clone https://github.com/mszuev/DataSorterShift.git
   cd DataSorterShift

2. Выполнить сборку Maven:
   ```bash
   mvn clean package

## Запуск
```bash
java -jar target/DataSorterShift.jar [options] <input-file1> [<input-file2> ...]
```

### Опции
- `-o <outputPath>`  
  Задать директорию для выходных файлов.  
  По умолчанию — текущая папка.

- `-p <prefix>`  
  Префикс для имён выходных файлов.  
  По умолчанию — пустая строка.

- `-a`  
  Режим **append**: добавлять к уже существующим файлам.
  По умолчанию — перезапись файлов.

- `-s`  
  Вывести **краткую** статистику - количество записанных элементов по каждому типу.

- `-f`  
  Вывести **полную** статистику:
  - для чисел: количество, минимальное, максимальное, сумма и среднее;
  - для строк: количество, длина самой короткой и самой длинной строки.

### Примеры

1. Фильтрация трёх файлов в текущую папку, перезапись и краткая статистика:
   ```bash
   java -jar target/DataSorterShift.jar -s input1.txt input2.txt input3.txt
2. Фильтрация с выводом в `/tmp/results`, префиксом `res_`, добавлением в файлы и полной статистикой:
   ```bash
   java -jar target/DataSorterShift.jar \
     -o /tmp/results \
     -p res_ \
     -a \
     -f \
     dataA.txt dataB.txt
