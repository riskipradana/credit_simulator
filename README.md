# Credit Simulator

A Java application to simulate vehicle credit/loan calculations. Supports interactive input and file-based input.

## Prerequisites

- **Java 21** or later
- **Apache Maven 3.6+**

Check versions:

```bash
java -version
mvn -version
```

## Build

From the project root:

```bash
mvn clean compile
```

Run tests:

```bash
mvn test
```

---

## How to Run

### 1. Run without file input (interactive)

Start the simulator and follow the prompts. You can use either the script or Maven.

**Using the script (recommended):**

```bash
./bin/credit_simulator
```

or:

```bash
bin/credit_simulator
```

**Using Maven directly:**

```bash
mvn -q exec:java -Dexec.mainClass="com.riski.pradana.credit.simulator.CreditSimulator"
```

**Step-by-step (interactive mode):**

1. Run one of the commands above.
2. You will see the menu:
   - **1** – Load Existing Credit  
   - **2** – Create Credit  
   - **3** – Exit  
3. Type **2** and press Enter to create a new credit.
4. Enter the following when prompted (one value per prompt):
   - **Jenis Kendaraan (Motor/Mobil):** `Motor` or `Mobil`
   - **Kondisi Kendaraan (NEW/USED):** `NEW` or `USED`
   - **Tahun Kendaraan (4 digit):** e.g. `2025`
   - **Jumlah Pinjaman (<=1,000,000,000):** e.g. `100000000`
   - **Tenor Pinjaman (1-6 tahun):** `1` to `6`
   - **Jumlah DP:** e.g. `35000000`
5. The program will print the installment schedule (per year: monthly amount and interest rate).
6. To run another calculation, choose **2** again; to quit, choose **3**.

---

### 2. Run with file input

Provide a text file whose lines are the same values you would type in interactive mode, in order (one value per line).

**Using the script (recommended):**

```bash
./bin/credit_simulator file_inputs.txt
```

or:

```bash
bin/credit_simulator file_inputs.txt
```

**Using Maven directly:**

```bash
mvn -q exec:java -Dexec.mainClass="com.riski.pradana.credit.simulator.CreditSimulator" -Dexec.args="file_inputs.txt"
```

**Step-by-step (file mode):**

1. Create a text file (e.g. `file_inputs.txt`) in the project root (or pass its path).
2. Add one value per line, in this order:
   - Menu choice: `1`, `2`, or `3`
   - If you use **2** (Create Credit), add the next 6 lines:
     - Vehicle type: `Mobil` or `Motor`
     - Condition: `NEW` or `USED`
     - Year (4 digits): e.g. `2025`
     - Total loan: e.g. `100000000`
     - Tenor (1–6): e.g. `5`
     - Down payment (DP): e.g. `35000000`
   - Then add the next menu choice again (e.g. `2` for another credit or `3` to exit).
3. Run: `./bin/credit_simulator file_inputs.txt` (or your filename).
4. The program reads from the file and prints the same output as in interactive mode. No prompts are printed for the values read from the file.

**Example `file_inputs.txt`:**

```
2
Mobil
NEW
2025
100000000
5
35000000
3
```

This means: choose **2** (Create Credit), use Mobil / NEW / 2025 / 100000000 / 5 / 35000000, then choose **3** (Exit).

---

## File input format reference

| Line order | Meaning              | Example values                    |
|-----------:|----------------------|-----------------------------------|
| 1          | Menu choice          | `1`, `2`, or `3`                 |
| 2 (if 2)   | Vehicle type         | `Mobil`, `Motor`                  |
| 3          | Condition            | `NEW`, `USED`                   |
| 4          | Vehicle year         | `2025`                            |
| 5          | Total loan           | `100000000`                       |
| 6          | Tenor (years)        | `1`–`6`                           |
| 7          | Down payment (DP)    | e.g. `35000000`                   |
| 8          | Next menu choice     | `1`, `2`, or `3` (repeat as needed) |

---

## Rules (validation)

- **Vehicle type:** Motor or Mobil only.  
- **Condition:** NEW or USED only.  
- **NEW vehicles:** Year must not be older than last year.  
- **Tenor:** 1–6 years.  
- **Total loan:** Between 1 and 1,000,000,000.  
- **NEW + Mobil:** DP at least 35% of total loan.  
- **NEW + Motor:** DP at least 25% of total loan.  

---

## Quick reference

| Goal                    | Command                              |
|-------------------------|--------------------------------------|
| Interactive run         | `./bin/credit_simulator` or `bin/credit_simulator` |
| Run with input file     | `./bin/credit_simulator file_inputs.txt` or `bin/credit_simulator file_inputs.txt` |
| Run with custom file    | `./bin/credit_simulator path/to/your_inputs.txt`   |
| Compile                 | `mvn compile`                        |
| Test                    | `mvn test`                           |