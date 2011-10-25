for file in `ls grc`
do
  echo mysqlimport --fields-terminated-by=@#$ -u webuser -pwebuser sor ../../../../perseus/hopper/sgml/reading/stats/grc/$file
  mysqlimport --fields-terminated-by=@#$ -u webuser -pwebuser sor ../../../../perseus/hopper/sgml/reading/stats/grc/$file
done

