#!/bin/bash
cat en_stopwords_2.raw | tr " " "\n" | sed "s/[	 ]*//" | sort  > en-2_stopwords.txt
