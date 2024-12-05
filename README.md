# JGrep - A Java Implementation of grep

JGrep is a simplified Java implementation of the Unix `grep` command line utility. It searches input files for lines containing a match to a specified pattern.

## Features

- Search for patterns in text files using regular expressions or fixed strings
- Support for multiple files
- Case-sensitive and case-insensitive searching
- Line number display
- Configurable filename display in output

## Installation

1. Ensure you have Java Development Kit (JDK) installed on your system
2. Clone this repository

## Usage

```bash
jgrep [OPTIONS] PATTERN FILE...
```

### Options

- `-H` : Print the filename for each match
- `-h` : Suppress the prefixing filename on output
- `-F, --fixed-strings` : Interpret pattern as a list of fixed strings
- `-i, --ignore-case` : Ignore case distinctions in pattern
- `-n, --line-number` : Print line number with output lines
- `--help` : Print help message

### Examples

Search for "hello" in file.txt:
```bash
./jgrep "hello" file.txt
```

Search case-insensitively for "HELLO" in multiple files:
```bash
./jgrep -i "HELLO" file1.txt file2.txt
```

Search with line numbers:
```bash
./jgrep -n "pattern" file.txt
```

## Exit Status

- Returns 0 if matches were found
- Returns 1 if no matches were found or if there was an error
