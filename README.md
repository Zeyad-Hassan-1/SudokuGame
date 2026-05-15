# SudokuVerifier
# Sudoku Game (Java)

A Java-based Sudoku game with GUI support, puzzle verification, difficulty generation, solving, and undo functionality.

## Project Overview

This project implements a complete Sudoku game system following MVC (Model-View-Controller) architecture and design pattern principles.

The system can:

- Verify Sudoku board validity
- Detect incomplete boards
- Generate Sudoku puzzles with multiple difficulty levels
- Save and load game progress
- Allow users to verify their moves
- Solve puzzles when only 5 cells remain
- Support Undo functionality
- Provide a graphical user interface (GUI)

---

## Features

### 1. Sudoku Verification
The system verifies whether a Sudoku board is:

- Valid → Correct and complete
- Invalid → Contains duplicate values
- Incomplete → Contains empty cells (0)

If the source Sudoku solution is invalid or incomplete, an exception is raised. :contentReference[oaicite:0]{index=0}

---

### 2. Difficulty Levels

The game automatically generates three difficulty levels by removing random cells from a solved Sudoku board:

- Easy → Remove 10 cells
- Medium → Remove 20 cells
- Hard → Remove 25 cells :contentReference[oaicite:1]{index=1}

Random cell selection is generated using a time-based random seed.

---

### 3. Game Storage

Generated games are stored in separate folders:

`text
easy/
medium/
hard/
current/
