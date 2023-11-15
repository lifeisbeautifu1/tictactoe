

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


package ru.tinkoff.tictactoebottinkoffsummerpractice.bot;

import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;
import java.util.Random;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

public class AppModel {
	private int[][] matrix;
	private int n;
	private int m;
	private int size;
	private int me;
	private HashMap<Integer, HashMap<Integer, Cell>> hashStep;
	private List<TurnPattern> prePattern;
	private List<Integer> patternWeight;
	private List<Pattern> patternForX;
	private List<Pattern> patternForO;
	private List<Direction> directions;
	private Pattern[] patternWin;

	public AppModel(String figure) {
		this.size = 15;
		this.matrix = new int[this.size][this.size];
		this.n = -1;
		this.m = -1;

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

		this.patternWin = new Pattern[]{
			Pattern.compile("0"),
			Pattern.compile("(1){5}"),
			Pattern.compile("(2){5}"),
			Pattern.compile("[01]*7[01]*"),
			Pattern.compile("[02]*7[02]*"),
		};

		this.patternWeight = new ArrayList<>();
		this.patternForX = new ArrayList<>();
		this.patternForO = new ArrayList<>();

		for (var pattern : this.prePattern) {
            String s = pattern.s;

			List<String> a = new ArrayList<>();

			int pos = -1;
			while ((pos = s.indexOf('x', pos + 1)) != -1) {
				a.add(s.substring(0, pos) + "7" + s.substring(pos + 1));
			}

			s = String.join("|", a);

			patternWeight.add(pattern.w);
			patternForX.add(Pattern.compile(s.replace("x", "1")));
			patternForO.add(Pattern.compile(s.replace("x", "2")));
		}

		// for (var weight : this.patternWeight) {
		// 	System.out.println("PATERN WEIGHT " + weight);
		// }
		// for (var p : this.patternForX) {
		// 	System.out.println("PATERN FOR X " + p);
		// }
		// for (var p : this.patternForO) {
		// 	System.out.println("PATERN FOR O " + p);
		// }

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

		// System.out.println("DIRECTIONS!!!");
		// for (Direction direction : this.directions) {
		// 	System.out.println(direction);
		// }

		this.hashStep = new HashMap<Integer, HashMap<Integer, Cell>>();

		// первый шаг, если АИ играет за Х
		var value = new HashMap<Integer, Cell>();
		value.put(7, new Cell(0, 1, 0, 0, 0));
		this.hashStep.put(7, value);
	}

	public void sync(String gameField) {
		for (int i = 0; i < 15; ++i) {
			for (int j = 0; j < 15; ++j) {
				switch(gameField.charAt(i * 15 + j)) {
					case 'x':
						if (this.matrix[i][j] != 1) {
							this.matrix[i][j] = 1;

							this.n = i;
							this.m = j;

							if (this.hashStep.containsKey(this.n)) {
								var entry = this.hashStep.get(this.n);
								if (entry.containsKey(this.m)) {
									entry.remove(this.m);
								}
							}
						}
						break;
					case 'o':
						if (this.matrix[i][j] != 2) {
							this.matrix[i][j] = 2;
							this.n = i;
							this.m = j;

							if (this.hashStep.containsKey(this.n)) {
								var entry = this.hashStep.get(this.n);
								if (entry.containsKey(this.m)) {
									entry.remove(this.m);
								}
							}
						}
						break;
				}
			}
		}

		if (this.n != -1 && this.m != -1) {
			this.calculateHashMove(false);
		}
	}

	public void show() {
		for (int i = 0; i < 15; ++i) {
			for (int j = 0; j < 15; ++j) {
				System.out.print(this.matrix[i][j]);
			}
			System.out.println("");
		}
	}

    public void moveAI() {
        int max = 0;

        this.calculateHashMovePattern();

        for (HashMap<Integer, Cell> row : hashStep.values()) {
            for (Cell cell : row.values()) {
                if (cell.sum > max)
                    max = cell.sum;
            }
        }

		var goodmoves = new ArrayList<Move>();

        for (HashMap.Entry<Integer, HashMap<Integer, Cell>> entry : hashStep.entrySet()) {
            int n = entry.getKey();

            HashMap<Integer, Cell> row = entry.getValue();

            for (int m : row.keySet()) {
                if (row.get(m).sum == max) {
                    goodmoves.add(new Move(n, m));
                }
            }
        }

		//DEBUG moveAI
		// for (HashMap.Entry<Integer, HashMap<Integer, Cell>> entry : hashStep.entrySet()) {
        //     int n = entry.getKey();

        //     HashMap<Integer, Cell> row = entry.getValue();

        //     for (int m : row.keySet()) {
        //         System.out.println("HELLO hashStep(n, m) = " + n + " " + m);
		// 		System.out.println(row.get(m));
        //     }
        // }

        Move move = goodmoves.get(new Random().nextInt(goodmoves.size()));

		this.n = move.n;
		this.m = move.m;
    }

	private void calculateHashMovePattern() {
		int attack;
		int defence;

		if (this.me == 1) {
			attack = 1;
			defence = 2;
		} else {
			attack = 2;
			defence = 1;
		}

		for (HashMap.Entry<Integer, HashMap<Integer, Cell>> entry : hashStep.entrySet()) {
            int n = entry.getKey();

            HashMap<Integer, Cell> row = entry.getValue();

            for (HashMap.Entry<Integer, Cell> cellEntry : row.entrySet()) {

                int m = cellEntry.getKey();

                Cell cell = cellEntry.getValue();

                cell.sum = cell.attack + cell.defence;
                cell.attackPattern = 0;
                cell.defencePattern = 0;

                for (int q = 1; q <= 2; q++) {
                    for (int j = 1; j <= 4; j++) {
                        StringBuilder s = new StringBuilder();
                        for (int i = -4; i <= 4; i++) {
                            switch (j) {
                                case 1:
                                    if (n + i >= 0 && n + i < this.size)
                                        s.append(i == 0 ? "7" : this.matrix[n + i][m]);
                                    break;
                                case 2:
                                    if (m + i >= 0 && m + i < this.size)
                                        s.append(i == 0 ? "7" : this.matrix[n][m + i]);
                                    break;
                                case 3:
                                    if (n + i >= 0 && n + i < this.size && m + i >= 0 && m + i < this.size)
                                        s.append(i == 0 ? "7" : this.matrix[n + i][m + i]);
                                    break;
                                case 4:
                                    if (n - i >= 0 && n - i < this.size && m + i >= 0 && m + i < this.size)
                                        s.append(i == 0 ? "7" : this.matrix[n - i][m + i]);
                                    break;
                            }
                        }

                        Pattern pattern = q == 1 ? this.patternWin[2 + attack] : this.patternWin[2 + defence];

						Matcher matcher = pattern.matcher(s);

						// System.out.println("STRING is " + s);

						if (matcher.find()) {
							// System.out.println("FOUND SOMETHING!!!");
							String res = s.substring(matcher.start(), matcher.end());

							// System.out.println("RESULT: " + res);

							if (res.length() < 5) {
								continue;
							}
							if (q == 1) {
								if (attack == 1) {
									for (int k = 0; k < this.patternForX.size(); ++k) {
										if (this.patternForX.get(k).matcher(s).find()) {
											cell.attackPattern += this.patternWeight.get(k);
										}
									}
								} else {
									for (int k = 0; k < this.patternForO.size(); ++k) {
										if (this.patternForO.get(k).matcher(s).find()) {
											cell.attackPattern += this.patternWeight.get(k);
										}
									}
								}
							} else {
								if (attack == 1) {
									for (int k = 0; k < this.patternForO.size(); ++k) {
										if (this.patternForO.get(k).matcher(s).find()) {
											cell.defencePattern += this.patternWeight.get(k);
										}
									}
								} else {
									for (int k = 0; k < this.patternForX.size(); ++k) {
										if (this.patternForX.get(k).matcher(s).find()) {
											cell.defencePattern += this.patternWeight.get(k);
										}
									}
								}

							}
						}
                    }
                }
                cell.sum += 1.1 * cell.attackPattern + cell.defencePattern;
            }
        }
    }

	private void calculateHashMove(boolean attack) {
        for (Direction direction : this.directions) {
            int n = this.n + direction.n;
            int m = this.m + direction.m;

            if (n < 0 || m < 0 || n >= this.size || m >= this.size)
                continue;

            if (matrix[n][m] != 0)
                continue;

            hashStep.computeIfAbsent(n, k -> new HashMap<>());
			hashStep.get(n).computeIfAbsent(m, k -> new Cell(0, 0, 0, 0, 0));

            if (attack)
                hashStep.get(n).get(m).attack += direction.w;
            else
                hashStep.get(n).get(m).defence += direction.w;
        }
    }

	public void makeTurn() {
		this.moveAI();

		// !
		if (this.hashStep.containsKey(this.n)) {
			var entry = this.hashStep.get(this.n);

			if (entry.containsKey(this.m)) {
				entry.remove(this.m);
			}
		}

		this.matrix[this.n][this.m] = this.me;

		this.calculateHashMove(true);
	}

	public String getGameField() {
		StringBuilder gameField = new StringBuilder();

		for (int i = 0; i < 15; ++i) {
			for (int j = 0; j < 15; ++j) {
				switch(this.matrix[i][j]) {
					case 0:
						gameField.append('_');
						break;
					case 1:
						gameField.append('x');
						break;
					case 2:
						gameField.append('o');
						break;
				}
			}
		}

		return gameField.toString();
	}

	private class TurnPattern {
        String s;
        int w;

        public TurnPattern(String s, int w) {
            this.s = s;
            this.w = w;
        }

		public String toString() {
			return "TURN PATTERN: s: " + this.s + " w: " + this.w +  "\n";
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
			return "Direction: n: " + this.n + " m: " + this.m + " w: " + this.w + "\n";
		}
    }

	private class Cell {
        int sum;
        int attack;
        int defence;
        int attackPattern;
        int defencePattern;

        public Cell(int sum, int attack, int defence, int attackPattern, int defencePattern) {
            this.sum = sum;
            this.attack = attack;
            this.defence = defence;
            this.attackPattern = attackPattern;
            this.defencePattern = defencePattern;
        }

		public String toString() {
			return "CELL: sum: " + this.sum + " attack: " + this.attack + " defence: " + this.defence + " attackPattern: " + this.attackPattern + " defencePattern: " + this.defencePattern +  "\n";
		}
    }

	private class Move {
		int n;
		int m;

		public Move(int n, int m) {
			this.n = n;
			this.m = m;
		}

		public String toString() {
			return "MOVE: n: " + this.n + " m: " + this.m +  "\n";
		}
	}

}
