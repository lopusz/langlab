#!/bin/bash
cat en-STAR_stopwords.raw | tr " " "\n" | sed "s/[	 ]*//" | sort  > en-2_stopwords.txt
