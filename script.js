const GEMI_SAYISI = 5;
let playerShips = [];
let botShips = [];
let botShots = [];
let gameStarted = false;
let playerHits = 0;
let botHits = 0;

// Tabloları oluştur
function createBoard(elementId, isBot) {
    const board = document.getElementById(elementId);
    for (let i = 0; i < 11; i++) {
        for (let j = 0; j < 11; j++) {
            const cell = document.createElement('div');
            cell.classList.add('cell');
            if (i === 0 && j === 0) cell.innerText = "X";
            else if (i === 0) cell.innerText = String.fromCharCode(64 + j);
            else if (j === 0) cell.innerText = i;
            else {
                cell.dataset.row = i;
                cell.dataset.col = j;
                if (isBot) cell.onclick = () => playerShoot(i, j, cell);
                else cell.onclick = () => placeShip(i, j, cell);
            }
            if (i === 0 || j === 0) cell.classList.add('header');
            board.appendChild(cell);
        }
    }
}

// Gemi Yerleştirme (Oyuncu)
function placeShip(r, c, cell) {
    if (gameStarted || playerShips.length >= GEMI_SAYISI) return;
    const coord = `${r},${c}`;
    if (!playerShips.includes(coord)) {
        playerShips.push(coord);
        cell.classList.add('ship');
        if (playerShips.length === GEMI_SAYISI) {
            gameStarted = true;
            document.getElementById('message').innerText = "Oyun Başladı! Düşman denizine ateş et.";
            createBotShips();
        }
    }
}

// Bot Gemilerini Oluştur (Rastgele)
function createBotShips() {
    while (botShips.length < GEMI_SAYISI) {
        const r = Math.floor(Math.random() * 10) + 1;
        const c = Math.floor(Math.random() * 10) + 1;
        const coord = `${r},${c}`;
        if (!botShips.includes(coord)) botShips.push(coord);
    }
}

// Oyuncunun Atışı
function playerShoot(r, c, cell) {
    if (!gameStarted || cell.classList.contains('hit') || cell.classList.contains('miss')) return;

    if (botShips.includes(`${r},${c}`)) {
        cell.classList.add('hit');
        playerHits++;
        document.getElementById('p-score').innerText = playerHits;
        checkWinner();
    } else {
        cell.classList.add('miss');
        setTimeout(botTurn, 500); // Iskalarsa sıra bota geçer
    }
}

// Botun Atışı (Java'daki getSmartRandomCoordinate mantığı)
function botTurn() {
    let r, c, coord;
    do {
        r = Math.floor(Math.random() * 10) + 1;
        c = Math.floor(Math.random() * 10) + 1;
        coord = `${r},${c}`;
    } while (botShots.includes(coord));

    botShots.push(coord);
    const playerBoardCells = document.querySelectorAll('#player-board .cell');
    const targetCell = Array.from(playerBoardCells).find(el => el.dataset.row == r && el.dataset.col == c);

    if (playerShips.includes(coord)) {
        targetCell.classList.add('hit');
        botHits++;
        document.getElementById('b-score').innerText = botHits;
        if (!checkWinner()) setTimeout(botTurn, 700); // Vurursa tekrar ateş eder
    } else {
        targetCell.classList.add('miss');
    }
}

function checkWinner() {
    if (playerHits === GEMI_SAYISI) {
        alert("TEBRİKLER! Kazandın.");
        location.reload();
        return true;
    } else if (botHits === GEMI_SAYISI) {
        alert("ÜZGÜNÜM... Bot kazandı.");
        location.reload();
        return true;
    }
    return false;
}

// Modal Elementlerini Al
const modal = document.getElementById("code-modal");
const btn = document.getElementById("source-code-btn");
const span = document.getElementsByClassName("close")[0];

// Butona basınca aç
btn.onclick = function() {
    modal.style.display = "block";
}

// (x) işaretine basınca kapat
span.onclick = function() {
    modal.style.display = "none";
}

// Modal dışına (boşluğa) basınca kapat
window.onclick = function(event) {
    if (event.target == modal) {
        modal.style.display = "none";
    }
}



// Başlat
createBoard('player-board', false);
createBoard('bot-board', true);