#!/bin/sh
apt-get update && apt-get install -y -qq lftp

# block support upload
lftp -c "set ftp:ssl-allow no; open -u $APIDOCS_USER,$APIDOCS_PASS andrei1058.com; mirror -Rev block-version/target/apidocs ./$CI_PROJECT_TITLE  --ignore-time --parallel=10 --exclude-glob .git* --exclude .git/"

# chat support upload
lftp -c "set ftp:ssl-allow no; open -u $APIDOCS_USER,$APIDOCS_PASS andrei1058.com; mirror -Rev chat-version/target/apidocs ./$CI_PROJECT_TITLE  --ignore-time --parallel=10 --exclude-glob .git* --exclude .git/"

# cmd support upload
lftp -c "set ftp:ssl-allow no; open -u $APIDOCS_USER,$APIDOCS_PASS andrei1058.com; mirror -Rev cmd_version/target/apidocs ./$CI_PROJECT_TITLE  --ignore-time --parallel=10 --exclude-glob .git* --exclude .git/"

# item stack support upload
lftp -c "set ftp:ssl-allow no; open -u $APIDOCS_USER,$APIDOCS_PASS andrei1058.com; mirror -Rev itemstack-version/target/apidocs ./$CI_PROJECT_TITLE  --ignore-time --parallel=10 --exclude-glob .git* --exclude .git/"

# material support upload
lftp -c "set ftp:ssl-allow no; open -u $APIDOCS_USER,$APIDOCS_PASS andrei1058.com; mirror -Rev material-version/target/apidocs ./$CI_PROJECT_TITLE  --ignore-time --parallel=10 --exclude-glob .git* --exclude .git/"

# particle support upload
lftp -c "set ftp:ssl-allow no; open -u $APIDOCS_USER,$APIDOCS_PASS andrei1058.com; mirror -Rev particle-version/target/apidocs ./$CI_PROJECT_TITLE  --ignore-time --parallel=10 --exclude-glob .git* --exclude .git/"

# sound support upload
lftp -c "set ftp:ssl-allow no; open -u $APIDOCS_USER,$APIDOCS_PASS andrei1058.com; mirror -Rev sounds-version/target/apidocs ./$CI_PROJECT_TITLE  --ignore-time --parallel=10 --exclude-glob .git* --exclude .git/"

# title support upload
lftp -c "set ftp:ssl-allow no; open -u $APIDOCS_USER,$APIDOCS_PASS andrei1058.com; mirror -Rev title_version/target/apidocs ./$CI_PROJECT_TITLE  --ignore-time --parallel=10 --exclude-glob .git* --exclude .git/"










