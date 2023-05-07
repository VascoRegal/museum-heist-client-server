#!/bin/bash


find . -name "*.class" -type f -print0 | xargs -0 /bin/rm -f


sh server.sh General 4000 &
sh server.sh CollectionSite 4001 &
sh server.sh Parties 4002 &
sh server.sh Museum 4003 &

sh client.sh MasterThief &
sh client.sh OrdinaryThief &
