var chessMenImg = {}
chessMenImg["BB"] = "bishop_black.png"
chessMenImg["WB"] = "bishop_white.png"
chessMenImg["BK"] = "king_black.png"
chessMenImg["WK"] = "king_white.png"
chessMenImg["WKn"] = "kinght_white.png"
chessMenImg["BKn"] = "knight_black.png"
chessMenImg["BP"] = "pawn_black.png"
chessMenImg["WP"] = "pawn_white.png"
chessMenImg["BQ"] = "queen_black.png"
chessMenImg["WQ"] = "queen_white.png"
chessMenImg["BR"] = "rook_black.png"
chessMenImg["WR"] = "rook_white.png"
var globals = {}

class ChessBoard {
    constructor() {
        this.team = "WHITE"
        this.opponent = "BALCK"
        //To represent chessMen position
        this.board = [
            ["BR","BKn","BB","BK","BQ","BB","BKn","BR"],
            ["BP","BP","BP","BP","BP","BP","BP","BP"],
            ["","","","","","","",""],
            ["","","","","","","",""],
            ["","","","","","","",""],
            ["","","","","","","",""],
            ["WP","WP","WP","WP","WP","WP","WP","WP"],
            ["WR","WKn","WB","WQ","WK","WB","WKn","WR"]
        ];
        //To track each tile on the board. Basically contains <div>'s
        this.tiles = Array(8);
    }
}


function initChessTable() {
    let tiles = globals.chessBoard.tiles;
    let grid = document.getElementById("chess-board");
    let i = 1;
    for(let x = 0; x < 8; x++) {
        tiles[x] = Array(8);
        for(let y = 0; y < 8; y++) {
            let tile = document.createElement("div");
            tile.id = i++;
            tile.classList.add("tile");            
//            tile.setAttribute("onDrop", "drop(event)")
//            tile.setAttribute("ondragover", "allowDrop(event)")    

            if((x%2==0 && y%2 != 0) || (x%2!=0 && y%2==0)) {
                tile.classList.add("tile-black");
            }
            tiles[x][y] = tile;        
//            tile.ondrag = drag;




            console.log(x, y, tile.id)
            grid.appendChild(tile);
            tile = document.getElementById(i-1)
            if(x > 5){
                tile.draggable = true;
                tile.setAttribute("onDragStart", "drag(event)");
                tile.setAttribute("dragEnd", "allowDrop(event)");
            } else {
                tile.setAttribute("onDrop", "drop(event)");
                tile.setAttribute("onDragEnter", "allowDrop(event)");
                tile.setAttribute("onDragOver", "allowDrop(event)");
                tile.setAttribute("dragleave", "allowDrop(event)");
                tile.setAttribute("dragend", "allowDrop(event)");
            }


        }
    }
}

function allowDrop(ev) {
    ev.preventDefault();
    console.log("allowDrop")
  }
  

function showChessMen() {
    let chessMen = globals.chessBoard.board;
    console.log(chessMen);
    let tiles = globals.chessBoard.tiles;
    for(let x = 0; x < 8; x++) {
        for(let y = 0; y < 8; y++) {
            if(chessMen[x][y]){
                let chessPiece = document.createElement("div");
                console.log("Appending ", x, y, chessMen[x][y], tiles[x][y].id);
                chessPiece.classList.add(chessMen[x][y]);
                tiles[x][y].classList.add(chessMen[x][y]);
//                tiles[x][y].appendChild(chessPiece);
            }
        }
    }
}


var drag = function(ev) {
    ev.preventDefault();
    console.log("Drag");
    ev.dataTransfer.setData("tile-id", ev.target.id);
}
  
var drop = function(ev) {
    ev.preventDefault();
    console.log("drop")
    let tileId = ev.dataTransfer.getData("tile-id");
    let x = Math.floor(tileId / 8);
    let y = tileId * 8;
    console.log(tileId, x, y);
//    ev.target.appendChild(document.getElementById(data));
  }


globals.chessBoard = new ChessBoard()

//initChessTable();
//showChessMen();


// document.addEventListener("dragstart", (event) => { 
//     event.preventDefault();
//     event.dataTransfer.setData("x", "y");
//     console.log("dragStart")
// });
// document.addEventListener("dragleave", (event) => {
//     event.preventDefault();
//     console.log("dragLeave")
// });

// grid.addEventListener("drop", (event) => {
//     event.preventDefault();
//     console.log("drop")
// });

// document.addEventListener("dragend", (event) => { 
//     event.preventDefault();
//     console.log("dragEnd")
// });
// grid.addEventListener("dragenter", (event) => { 
//     event.preventDefault();
//     console.log("dragEnter")
// });
// grid.addEventListener("dragover", (event) => {
//      event.preventDefault();
//     console.log("dragOVer")
// });

