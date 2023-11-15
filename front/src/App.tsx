import { useState, useEffect } from "react";

function App() {
  const [gameField, setGameField] = useState(
    new Array(19).fill(0).map(() => new Array(19).fill("_"))
  );

  const [makeTurn, setMakeTurn] = useState(false);

  useEffect(() => {
    const fetchTurn = async () => {
      const res = await fetch("http://localhost:8081/bot/turn", {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
        },
        body: JSON.stringify({
          game_field: gameField.map((row) => row.join("")).join(""),
        }),
      });
      const data = await res.json();
      setGameField(
        data["game_field"].match(/.{1,19}/g).map((row) => row.split(""))
      );
      setMakeTurn(false);
    };
    if (makeTurn) fetchTurn();
  }, [makeTurn, gameField]);

  return (
    <div style={{ display: "flex", flexDirection: "column" }}>
      {gameField.map((row, rowIndex) => {
        return (
          <div key={rowIndex}>
            {row.map((char, colIndex) => (
              <input
                maxLength={1}
                value={char}
                key={19 * rowIndex + colIndex}
                onChange={(event) => {
                  setGameField((p) =>
                    p.map((row, rowIdx) =>
                      rowIdx === rowIndex
                        ? row.map((value, colIdx) =>
                            colIdx === colIndex ? event.target.value : value
                          )
                        : row
                    )
                  );
                }}
              />
            ))}
          </div>
        );
      })}
      <button style={{ marginTop: 20 }} onClick={() => setMakeTurn(true)}>
        make turn
      </button>
    </div>
  );
}

export default App;
