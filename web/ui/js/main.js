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
        this.tileIdPieceMap = Array(64);
    }
}


function initChessTable() {
    let tiles = globals.chessBoard.tiles;
    let grid = document.getElementById("chess-board");
    let i = 0;
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




            console.log(x, y, tile.id);
            grid.appendChild(tile);
            tile.draggable = true;
            tile.setAttribute("onDragStart", "drag(event, " + tile.id +")");
            tile.setAttribute("onDrop", "drop(event)");
            tile.setAttribute("onDragEnter", "allowDrop(event)");
            tile.setAttribute("onDragOver", "allowDrop(event)");
            tile.setAttribute("dragleave", "allowDrop(event)");
            tile.setAttribute("dragend", "allowDrop(event)");
        }
    }
}

function allowDrop(ev) {
    ev.preventDefault();
  }
  

function showChessMen() {
    let chessMen = globals.chessBoard.board;
    let tileIdPieceMap = globals.chessBoard.tileIdPieceMap;
    console.log(chessMen);
    let tiles = globals.chessBoard.tiles;
    let id = 0;
    for(let x = 0; x < 8; x++) {
        for(let y = 0; y < 8; y++) {
            if(chessMen[x][y]){
                tiles[x][y].classList.add(chessMen[x][y]);
            }
            tileIdPieceMap[id] = chessMen[x][y];
            id++;
            
        }
    }
}


var drag = function(event, id) {
    console.log("Drag: ", id);
    event.dataTransfer.setData("tile-id", id);
    if(!globals.chessBoard.tileIdPieceMap[id]) {
        console.log("Looks like this tile is empty...");
    }
}
  
var drop = function(event) {
    event.preventDefault();
    let tileIdPieceMap =  globals.chessBoard.tileIdPieceMap;
    let board = globals.chessBoard.board;
    console.log("drop")
    let sourceTileId = event.dataTransfer.getData("tile-id");
    let destTile = event.target;
    let destTileId = destTile.id;
    console.log("source: ", sourceTileId)
    console.log("dest", destTileId);
    let sourceTile = document.getElementById(sourceTileId);
    sourceTile.classList.remove(tileIdPieceMap[sourceTileId])
    if(tileIdPieceMap[destTileId]){
        destTile.classList.remove(tileIdPieceMap[destTileId]);
    }
    destTile.classList.add(tileIdPieceMap[sourceTileId]);
    tileIdPieceMap[destTileId] = tileIdPieceMap[sourceTileId]
    tileIdPieceMap[sourceTileId] = "";

    let x1 = Math.floor(sourceTileId / 8);
    let y1 = sourceTileId % 8;
    let x2 = Math.floor(destTileId / 8);
    let y2 = destTileId % 8;

    board[x2][y2] = board[x1][y1];
    board[x1][y1] = "";
    console.log(board);
  }


globals.chessBoard = new ChessBoard()

initChessTable();
showChessMen();