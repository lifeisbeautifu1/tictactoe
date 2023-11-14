// package ru.tinkoff.tictactoebottinkoffsummerpractice.bot;

// import java.util.HashMap;
// import java.util.Map;
// import java.util.Random;
// import java.util.regex.Pattern;

// public class AppModel {
//     private int n;
//     private int m;
//     private int size = 19;
//     private boolean who; // Логическая переменная - кто сейчас ходит: true - X, false - O
//     private int[][] matrix; // Матрица игрового поля 19х19. 0 - свободная клетка, 1 - крестик, 2 - нолик
//     private int freeCells; // Количество свободных ячеек. В начале каждой игры = 361
//     private Map<Integer, Map<Integer, Cell>> hashStep; // Хеш-массив потенциальных ходов
//     private boolean playing; // True - игра в процессе игры (пользователь может кликать на поле и т.д.)
//     private int[] winLine; // Координаты победной линии
//     private Pattern[] patternWin; // Массив выигрышных шаблонов [1] и [2] и шаблон определения возможности поставить 5 в ряд
//     private Direction[] directions; // Направления расчета потенциальных ходов
//     private int step; // Счетчик ходов игры
//     private int[][] prePattern = { // Шаблоны построения фигрур и их веса. Х в дальнейшем заменяется на крестик (1) или нолик (0), 0 - свободная ячейка
//             {99999, 7000, 4000, 4000, 2000, 2000, 2000, 2000, 2000, 2000, 3000, 1500, 1500, 800, 800, 800, 800, 200},
//             {0b11111, 0b011110, 0b11110, 0b0111, 0b0110110, 0b0110110, 0b0110110, 0b0110110, 0b0110110, 0b0110110, 0b01110, 0b0111, 0b1110, 0b011010, 0b011010, 0b010110, 0b010110, 0b0110}
//     };
//     private int[][][] pattern; // Массив шаблонов для Х и 0, генерируется из предыдущих шаблонов

//     private class Cell {
//         int sum;
//         int attack;
//         int defence;
//         int attackPattern;
//         int defencePattern;

//         public Cell(int sum, int attack, int defence, int attackPattern, int defencePattern) {
//             this.sum = sum;
//             this.attack = attack;
//             this.defence = defence;
//             this.attackPattern = attackPattern;
//             this.defencePattern = defencePattern;
//         }
//     }

//     private class Direction {
//         int n;
//         int m;
//         int w;

//         public Direction(int n, int m, int w) {
//             this.n = n;
//             this.m = m;
//             this.w = w;
//         }
//     }

//     public AppModel() {
//         init();
//     }

//     private void init() {
//         hashStep = new HashMap<>();
//         patternWin = new Pattern[]{null, Pattern.compile("1{5}"), Pattern.compile("2{5}"), Pattern.compile("[01]*7[01]*"), Pattern.compile("[02]*7[02]*")};
//         directions = new Direction[24];
//         int index = 0;
//         for (int n = -2; n <= 2; n++) {
//             for (int m = -2; m <= 2; m++) {
//                 if (n == 0 && m == 0)
//                     continue;
//                 if (Math.abs(n) <= 1 && Math.abs(m) <= 1)
//                     directions[index++] = new Direction(n, m, 3);
//                 else if (Math.abs(n) == Math.abs(m) || n * m == 0)
//                     directions[index++] = new Direction(n, m, 2);
//                 else
//                     directions[index++] = new Direction(n, m, 1);
//             }
//         }
//     }

//     public void setStartData(int a) {
//         who = true;
//         matrix = new int[size][size];
//         winLine = new int[0];
//         hashStep.clear();
//         freeCells = size * size;
//         for (int n = 0; n < size; n++) {
//             for (int m = 0; m < size; m++) {
//                 matrix[n][m] = 0;
//             }
//         }
//         step = 0;
//         playing = true;
//         if (a == 2)
//             System.out.println("New Game! X - AI, O - user");
//         else
//             System.out.println("New Game! X - user, O - AI");
//     }

//     public void setNM(int a) {
//         n = a / size;
//         m = a % size;
//     }

//     public boolean emptyCell() {
//         return matrix[n][m] == 0;
//     }

//     public int moveAI() {
//         playing = false;
//         int max = 0;
//         calculateHashMovePattern();
//         for (Map<Integer, Cell> row : hashStep.values()) {
//             for (Cell cell : row.values()) {
//                 if (cell.sum > max)
//                     max = cell.sum;
//             }
//         }
//         int[][] goodmoves = new int[hashStep.size()][2];
//         int index = 0;
//         for (Map.Entry<Integer, Map<Integer, Cell>> entry : hashStep.entrySet()) {
//             int n = entry.getKey();
//             Map<Integer, Cell> row = entry.getValue();
//             for (int m : row.keySet()) {
//                 if (row.get(m).sum == max) {
//                     goodmoves[index][0] = n;
//                     goodmoves[index][1] = m;
//                     index++;
//                 }
//             }
//         }
//         int[] movenow = goodmoves[new Random().nextInt(index)];
//         n = movenow[0];
//         m = movenow[1];
//         return move(n, m, true);
//     }

//     private int move(int n, int m, boolean aiStep) {
//         hashStep.remove(n);
//         hashStep.values().forEach(row -> row.remove(m));
//         matrix[n][m] = 2 - (who ? 1 : 0);
//         who = !who;
//         freeCells--;
//         String[] s = new String[4];
//         int nT = Math.min(n, 4);
//         int nR = Math.min(size - m - 1, 4);
//         int nB = Math.min(size - n - 1, 4);
//         int nL = Math.min(m, 4);
//         for (int j = n - nT; j <= n + nB; j++)
//             s[0] += matrix[j][m];
//         for (int i = m - nL; i <= m + nR; i++)
//             s[1] += matrix[n][i];
//         for (int i = -Math.min(nT, nL); i <= Math.min(nR, nB); i++)
//             s[2] += matrix[n + i][m + i];
//         for (int i = -Math.min(nB, nL); i <= Math.min(nR, nT); i++)
//             s[3] += matrix[n - i][m + i];
//         int t = matrix[this.n][this.m];
//         int k;
//         if ((k = s[0].indexOf(patternWin[t].pattern())) >= 0)
//             winLine = new int[]{m, n - nT + k, m, n - nT + k + 4};
//         else if ((k = s[1].indexOf(patternWin[t].pattern())) >= 0)
//             winLine = new int[]{m - nL + k, n, m - nL + k + 4, n};
//         else if ((k = s[2].indexOf(patternWin[t].pattern())) >= 0)
//             winLine = new int[]{m - Math.min(nT, nL) + k, n - Math.min(nT, nL) + k, m - Math.min(nT, nL) + k + 4, n - Math.min(nT, nL) + k + 4};
//         else if ((k = s[3].indexOf(patternWin[t].pattern())) >= 0)
//             winLine = new int[]{m - Math.min(nB, nL) + k, n + Math.min(nB, nL) - k, m - Math.min(nB, nL) + k + 4, n + Math.min(nB, nL) - k - 4, -1};
//         playing = freeCells != 0 && winLine.length == 0;
//         if (playing)
//             calculateHashMove(aiStep);
//         System.out.println(++step + ": " + n + ", " + m);
//         return n * size + m;
//     }

//     private void calculateHashMove(boolean attack) {
//         for (Direction direction : directions) {
//             int n = this.n + direction.n;
//             int m = this.m + direction.m;
//             if (n < 0 || m < 0 || n >= size || m >= size)
//                 continue;
//             if (matrix[n][m] != 0)
//                 continue;
//             hashStep.computeIfAbsent(n, k -> new HashMap<>()).put(m, new Cell(0, 0, 0, 0, 0));
//             if (attack)
//                 hashStep.get(n).get(m).attack += direction.w;
//             else
//                 hashStep.get(n).get(m).defence += direction.w;
//         }
//     }

//     private void calculateHashMovePattern() {
//         for (Map.Entry<Integer, Map<Integer, Cell>> entry : hashStep.entrySet()) {
//             int n = entry.getKey();
//             Map<Integer, Cell> row = entry.getValue();
//             for (Map.Entry<Integer, Cell> cellEntry : row.entrySet()) {
//                 int m = cellEntry.getKey();
//                 Cell cell = cellEntry.getValue();
//                 cell.sum = cell.attack + cell.defence;
//                 cell.attackPattern = 0;
//                 cell.defencePattern = 0;
//                 for (int q = 1; q <= 2; q++) {
//                     for (int j = 1; j <= 4; j++) {
//                         StringBuilder s = new StringBuilder();
//                         for (int i = -4; i <= 4; i++) {
//                             switch (j) {
//                                 case 1:
//                                     if (n + i >= 0 && n + i < size)
//                                         s.append(i == 0 ? '7' : matrix[n + i][m]);
//                                     break;
//                                 case 2:
//                                     if (m + i >= 0 && m + i < size)
//                                         s.append(i == 0 ? '7' : matrix[n][m + i]);
//                                     break;
//                                 case 3:
//                                     if (n + i >= 0 && n + i < size && m + i >= 0 && m + i < size)
//                                         s.append(i == 0 ? '7' : matrix[n + i][m + i]);
//                                     break;
//                                 case 4:
//                                     if (n - i >= 0 && n - i < size && m + i >= 0 && m + i < size)
//                                         s.append(i == 0 ? '7' : matrix[n - i][m + i]);
//                                     break;
//                             }
//                         }
//                         Pattern pattern = q == 1 ? Pattern.compile(Integer.toBinaryString(prePattern[q][j])) : Pattern.compile(Integer.toBinaryString(prePattern[q][j]), Pattern.LITERAL);
//                         if (pattern.matcher(s).find()) {
//                             if (q == 1) {
//                                 for (int i = 0; i < pattern[0].length; i++) {
//                                     if (pattern[0][i] == '1')
//                                         cell.attackPattern += prePattern[0][i];
//                                 }
//                             } else {
//                                 for (int i = 0; i < pattern[0].length; i++) {
//                                     if (pattern[0][i] == '1')
//                                         cell.defencePattern += prePattern[0][i];
//                                 }
//                             }
//                         }
//                     }
//                 }
//                 cell.sum += 1.1 * cell.attackPattern + cell.defencePattern;
//             }
//         }
//     }
// }
package ru.tinkoff.tictactoebottinkoffsummerpractice.bot;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class AppModel {
	// Game Matrix 19x19
	// 0 - free cell, 1 - x, 2 - o
	private int[][] matrix;
	// Current row index
	private int n;
	// Current col index
	private int m;
	// Amount of free cells
	private int freeCells;
	// Playing figure
	private int me;
	private List<TurnPattern> prePattern;
	private List<Integer> patternWeight;
	private List<Pattern> patternForX;
	private List<Pattern> patternForO;
	private List<Direction> directions;

	public AppModel(String figure) {
		this.matrix = new int[19][19];
		this.freeCells = 19 * 19;
		this.n = 0;
		this.m = 0;

		switch(figure) {
			case "x":
				this.me = 1;
				break;
			case "o":
				this.me = 2;
				break;
		}

		this.prePattern = new ArrayList<>();
        prePattern.add(new TurnPattern("xxxxx", 99999));
        prePattern.add(new TurnPattern("0xxxx0", 7000));
        prePattern.add(new TurnPattern("0xxxx", 4000));
        prePattern.add(new TurnPattern("xxxx0", 4000));
        prePattern.add(new TurnPattern("0x0xxx", 2000));
        prePattern.add(new TurnPattern("0xx0xx", 2000));
        prePattern.add(new TurnPattern("0xxx0x", 2000));
        prePattern.add(new TurnPattern("xxx0x0", 2000));
        prePattern.add(new TurnPattern("xx0xx0", 2000));
        prePattern.add(new TurnPattern("x0xxx0", 2000));
        prePattern.add(new TurnPattern("0xxx0", 3000));
        prePattern.add(new TurnPattern("0xxx", 1500));
        prePattern.add(new TurnPattern("xxx0", 1500));
        prePattern.add(new TurnPattern("0xx0x", 800));
        prePattern.add(new TurnPattern("0x0xx", 800));
        prePattern.add(new TurnPattern("xx0x0", 800));
        prePattern.add(new TurnPattern("x0xx0", 800));
        prePattern.add(new TurnPattern("0xx0", 200));

		this.patternWeight = new ArrayList<>();
		this.patternForX = new ArrayList<>();
		this.patternForO = new ArrayList<>();

		for (var pattern : this.prePattern) {
            String s = pattern.s;

			List<String> a = new ArrayList<>();

			int pos = -1;
			while ((pos = s.indexOf('x', pos + 1)) != -1) {
				a.add(s.substring(0, pos) + '7' + s.substring(pos + 1));
			}

			s = String.join("|", a);

			patternWeight.add(pattern.w);
			patternForX.add(Pattern.compile(s.replace("x", "1")));
			patternForO.add(Pattern.compile(s.replace("x", "2")));
		}

		this.directions = new ArrayList<>();

		for (int n = -2; n <= 2; n++) {
        	for (int m = -2; m <= 2; m++) {
                if (n == 0 && m == 0)
                    continue;
                if (Math.abs(n) <= 1 && Math.abs(m) <= 1)
                    this.directions.add(new Direction(n, m, 3));
                else if (Math.abs(n) == Math.abs(m) || n * m == 0)
                    this.directions.add(new Direction(n, m, 2));
                else
                    this.directions.add(new Direction(n, m, 1));
            }
        }
	}

	public void sync(String gameField) {
		for (int i = 0; i < 19; ++i) {
			for (int j = 0; j < 19; ++j) {
				switch(gameField.charAt(i * 19 + j)) {
					case 'x':
						if (this.matrix[i][j] != 1) {
							this.matrix[i][j] = 1;
							this.freeCells -= 1;
						}
						break;
					case 'o':
						if (this.matrix[i][j] != 2) {
							this.matrix[i][j] = 2;
							this.freeCells -= 1;
						}
						break;
				}
			}
		}
	}

	public void show() {
		for (int i = 0; i < 19; ++i) {
			for (int j = 0; j < 19; ++j) {
				System.out.print(this.matrix[i][j]);
			}
			System.out.println("");
		}

		System.out.println("Free cells: " + this.freeCells);
	}

	public void makeTurn() {
		this.matrix[this.n][this.m] = this.me;
		this.n += 1;
		this.m += 1;
		this.freeCells -= 1;
	}

	public String getGameField() {
		String gameField = "";

		for (int i = 0; i < 19; ++i) {
			for (int j = 0; j < 19; ++j) {
				switch(this.matrix[i][j]) {
					case 0:
						gameField += '_';
						break;
					case 1:
						gameField += 'x';
						break;
					case 2:
						gameField += 'o';
						break;
				}
			}
		}

		return gameField;
	}

	private class TurnPattern {
        String s;
        int w;

        public TurnPattern(String s, int w) {
            this.s = s;
            this.w = w;
        }
    }

	private class Direction {
        int n;
        int m;
        int w;

        public Direction(int n, int m, int w) {
            this.n = n;
            this.m = m;
            this.w = w;
        }

		public String toString() {
			return "n: " + this.n + " m: " + this.m + " w: " + this.w + "\n";
		}
    }

}
