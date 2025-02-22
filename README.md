# Monieshop Analytics

## What is this?
This project reads sales data from text files for a shop called Monieshop and tells you:
- The day with the most items sold.
- The day that made the most money.
- The product people bought the most.
- The best sales staff for each month.
- The busiest hour of the day.

## How does it work?
- There are 365 text files, one for each day of the year 2025 (e.g., `2025-01-01.txt`).
- Each file has one line of information with the sales staff ID, transaction time, products sold, and total sales amount.
- The code reads all these files, creates objects for each day's sales, and then calculates useful information.

## How to run the code
1. Make sure you have all the 365 text files in the same folder as the code.
2. Compile the code:
   ```
   javac Main.java Monieshop.java
   ```
3. Run the code:
   ```
   java Main
   ```
4. The analytics report will show up in the terminal.

## Why use this?
It helps you understand what days were busy, which products sold the most, and who performed best in sales without going through all the files by hand.