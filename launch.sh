#!/bin/bash

# Vérification du nombre de paramètres
if [ "$#" -ne 3 ]; then
  echo "Usage: $0 param1 param2 param3"
  exit 1
fi

# Lecture des paramètres
mode="$1"
idWar="$2"
mail="$3"

echo "Paramètres reçus : $mode, $idWar, $mail"

sbt "run $mode $idWar $mail"