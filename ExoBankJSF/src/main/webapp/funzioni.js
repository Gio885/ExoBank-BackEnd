var cellaSelezionata = null;
var doppioClic = false;

function selezionaCella(button) {
	    console.log('Doppio clic rilevato');

    if (!doppioClic) {
        // Primo clic
        cellaSelezionata = cell;
        doppioClic = true;

        setTimeout(function() {
            if (doppioClic) {
                // Doppio clic
                eseguiAzioneJS(cellaSelezionata);
                cellaSelezionata = null;
                doppioClic = false;
            } else {
                // Singolo clic
            }
        }, 300); // Imposta un ritardo per il rilevamento del doppio clic
    } else {
        // Secondo clic (ignorato)
        doppioClic = false;
    }
}

function eseguiAzioneJS(cellaPartenza) {
    var coordinataPartenza = cellaPartenza.getAttribute('coordinata');
    var idPezzo = cellaPartenza.getAttribute('idPezzo');

    // Esegui una richiesta Ajax per passare i dati al tuo bean
    PrimeFaces.ab({ 
        source: '', // L'id del componente sorgente, se necessario
        process: '@form', // L'id del componente da processare prima dell'invio
        update: 'homeForm:scacchiera', // L'id del componente da aggiornare con il risultato
        formId: 'homeForm', // L'id del form o del componente padre
        oncomplete: function(xhr, status, args) {
            // Questo è il callback che viene chiamato dopo la richiesta Ajax
            // args contiene i dati restituiti dal bean, se necessario
        },
        params: [
            {name: 'coordinataPartenza', value: coordinataPartenza},
            {name: 'idPezzo', value: idPezzo}
        ]
    });
}



function drag(event) {
	    console.log("Drag started");

    event.dataTransfer.setData("text", event.target.id);
}

function allowDrop(event) {
	    console.log("allowDrop started");

    event.preventDefault();
}

function drop(event) {
	    console.log("drop started");

    event.preventDefault();
    var data = event.dataTransfer.getData("text");
    var target = event.target;
    while (target.tagName !== "TD") {
        target = target.parentElement;
    }
    target.appendChild(document.getElementById(data));
}



function vittoria(){
alert("stampa fuori")
let W = window.innerWidth;
let H = window.innerHeight;
const canvas = document.getElementById("canvas");
const context = canvas.getContext("2d");
const maxConfettis = 150;
const particles = [];

const possibleColors = [
  "DodgerBlue",
  "OliveDrab",
  "Gold",
  "Pink",
  "SlateBlue",
  "LightBlue",
  "Gold",
  "Violet",
  "PaleGreen",
  "SteelBlue",
  "SandyBrown",
  "Chocolate",
  "Crimson"
];

function randomFromTo(from, to) {
  return Math.floor(Math.random() * (to - from + 1) + from);
}

function confettiParticle() {
  this.x = Math.random() * W; // x
  this.y = Math.random() * H - H; // y
  this.r = randomFromTo(11, 33); // radius
  this.d = Math.random() * maxConfettis + 11;
  this.color =
    possibleColors[Math.floor(Math.random() * possibleColors.length)];
  this.tilt = Math.floor(Math.random() * 33) - 11;
  this.tiltAngleIncremental = Math.random() * 0.07 + 0.05;
  this.tiltAngle = 0;

  this.draw = function() {
    context.beginPath();
    context.lineWidth = this.r / 2;
    context.strokeStyle = this.color;
    context.moveTo(this.x + this.tilt + this.r / 3, this.y);
    context.lineTo(this.x + this.tilt, this.y + this.tilt + this.r / 5);
    return context.stroke();
  };
}

function Draw() {
  const results = [];

  // Magical recursive functional love
  requestAnimationFrame(Draw);

  context.clearRect(0, 0, W, window.innerHeight);

  for (var i = 0; i < maxConfettis; i++) {
    results.push(particles[i].draw());
  }

  let particle = {};
  let remainingFlakes = 0;
  for (var i = 0; i < maxConfettis; i++) {
    particle = particles[i];

    particle.tiltAngle += particle.tiltAngleIncremental;
    particle.y += (Math.cos(particle.d) + 3 + particle.r / 2) / 2;
    particle.tilt = Math.sin(particle.tiltAngle - i / 3) * 15;

    if (particle.y <= H) remainingFlakes++;

    // If a confetti has fluttered out of view,
    // bring it back to above the viewport and let if re-fall.
    if (particle.x > W + 30 || particle.x < -30 || particle.y > H) {
      particle.x = Math.random() * W;
      particle.y = -30;
      particle.tilt = Math.floor(Math.random() * 10) - 20;
    }
  }

  return results;
}

window.addEventListener(
  "resize",
  function() {
    W = window.innerWidth;
    H = window.innerHeight;
    canvas.width = window.innerWidth;
    canvas.height = window.innerHeight;
  },
  false
);

// Push new confetti objects to `particles[]`
for (var i = 0; i < maxConfettis; i++) {
  particles.push(new confettiParticle());
}

// Initialize
canvas.width = W;
canvas.height = H;
Draw();

}

function startConfetti() {
				alert("stampa fuori")

            const confettiContainer = document.createElement('div');
            confettiContainer.className = 'confetti-container';

            const numConfetti = 100;
            for (let i = 0; i < numConfetti; i++) {
                const confetti = document.createElement('div');
                confetti.className = 'confetti';
                confetti.style.left = Math.random() * 100 + 'vw';
                confetti.style.animationDuration = Math.random() * 3 + 2 + 's';
                confetti.style.animationDelay = Math.random() * 2 + 's';
                confettiContainer.appendChild(confetti);
            }

            document.body.appendChild(confettiContainer);

            // Rimuove i coriandoli dopo l'animazione
            setTimeout(() => {
                document.body.removeChild(confettiContainer);
            }, 5000); // Cambia questo valore se vuoi che i coriandoli durino più o meno tempo
        }
