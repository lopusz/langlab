#/bin/bash

mkdir -p dict/libreoffice
cd dict/libreoffice
wget http://cgit.freedesktop.org/libreoffice/dictionaries/plain/pl_PL/pl_PL.dic
wget http://cgit.freedesktop.org/libreoffice/dictionaries/plain/pl_PL/pl_PL.aff

gzip pl_PL.aff 
gzip pl_PL.dic

cd ../../

mkdir -p dict/sjp
cd dict/sjp

wget -O- http://sjp.pl/slownik/ort/sjp-myspell-pl-20150428.zip \
   | bsdtar -O -xvf - pl_PL.zip \
   | bsdtar -xvf - pl_PL.aff pl_PL.dic

gzip pl_PL.aff 
gzip pl_PL.dic

