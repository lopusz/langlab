#!/bin/bash

cat stopwords_header.clj

FILES=`ls -1 *_stopwords.txt`

for F in ${FILES}; do
    VAR1=`echo ${F} | sed -e "s/_stopwords.txt//" | sed -e "s/-.*//"`
    VAR2=`echo ${F} | sed -e "s/_stopwords.txt//" \
                    | sed -e "s/^[^-]*//" \
                    | sed -e "s/-//" \
                    | sed -e "s/x/*/g"`
    echo "(defn ${VAR1}-get-stopwords${VAR2}-ranks"
    grep -e "^#" ${F} | sed -e "s/^#//"
    STOPWORDS=`grep -v -e "^#" ${F} | sort | uniq`
    LAST=`grep -v -e "^#" ${F} | sort | uniq | tail -n1`
    FIRST=`grep -v -e "^#" ${F} | sort | uniq | head -n1`
    for S in ${STOPWORDS}; do
        if [[ "${S}" == "${FIRST}" ]]; then
            echo "  []"
            echo "  #{ \"${S}\"" 
        elif [[ "${S}" == "${LAST}" ]]; then 
            echo "     \"${S}\"})"
            echo
        else
            echo "     \"${S}\""
        fi
    done
done
