#!/bin/bash

cat stopwords_header.clj

FILES=`ls -1 *_stopwords.txt`

for F in ${FILES}; do
    VAR=`echo ${F} | sed -e "s/_stopwords.txt//"`
    echo "(def ${VAR}-sw" \
        | sed "s/en-1-sw/en-sw/"  \
        | sed "s/en-2-sw/en-sw-1/" \
        | sed "s/en-3-sw/en-sw-2/"

    grep -e "^#" ${F} | sed -e "s/^#//"
    STOPWORDS=`grep -v -e "^#" ${F} | sort | uniq`
    LAST=`grep -v -e "^#" ${F} | sort | uniq | tail -n1`
    FIRST=`grep -v -e "^#" ${F} | sort | uniq | head -n1`
    for S in ${STOPWORDS}; do
        if [[ "${S}" == "${FIRST}" ]]; then
            echo "  #{ \"${S}\"" 
        elif [[ "${S}" == "${LAST}" ]]; then 
            echo "     \"${S}\"})"
            echo
        else
            echo "     \"${S}\""
        fi
    done
done
