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