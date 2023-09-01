function toClipBoard(newClip) {
    navigator.clipboard.writeText(newClip).then(
      function () {
        /* le presse-papier est correctement paramétré */
        alert("Copie ok")
      },
      function () {
        /* l'écriture dans le presse-papier a échoué */
        alert("Copie échec")
      },
    );
  }
  