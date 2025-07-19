#!/bin/bash

# Nom du volume Docker à créer
VOLUME_NAME="logs_sheet_gen_fuyuki"

# Vérifie si le volume existe
if docker volume inspect "$VOLUME_NAME" > /dev/null 2>&1; then
  echo "✅ Volume '$VOLUME_NAME' existe déjà."
else
  echo "📦 Volume '$VOLUME_NAME' introuvable. Création..."
  docker volume create "$VOLUME_NAME"

  # Vérifie que la création a réussi
  if [ $? -eq 0 ]; then
    echo "✅ Volume '$VOLUME_NAME' créé avec succès."
  else
    echo "❌ Échec de la création du volume '$VOLUME_NAME'." >&2
    exit 1
  fi
fi
