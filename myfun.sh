if [[ $# -lt 2 ]]; then
  echo "Controlla i parametri"
else
  java -jar -Dfile.encoding=UTF-8 -jar /home/vemanuele/IdeaProjects/Martone-Criscuolo_es5_scg/out/artifacts/Martone_Criscuolo_es5_scg_jar/Martone-Criscuolo_es5_scg.jar test $1
  gcc temp/test.c -o $2
fi