#!/bin/sh
apt-get update && apt-get install -y -qq lftp

# block support upload
lftp -c "set ftp:ssl-allow no; open -u $APIDOCS_USER,$APIDOCS_PASS andrei1058.com; mirror -Rev block_common/target/apidocs ./$CI_PROJECT_TITLE  --ignore-time --parallel=10 --exclude-glob .git* --exclude .git/"

# chat support upload
lftp -c "set ftp:ssl-allow no; open -u $APIDOCS_USER,$APIDOCS_PASS andrei1058.com; mirror -Rev chat_common/target/apidocs ./$CI_PROJECT_TITLE  --ignore-time --parallel=10 --exclude-glob .git* --exclude .git/"

# cmd support upload
lftp -c "set ftp:ssl-allow no; open -u $APIDOCS_USER,$APIDOCS_PASS andrei1058.com; mirror -Rev cmd_common/target/apidocs ./$CI_PROJECT_TITLE  --ignore-time --parallel=10 --exclude-glob .git* --exclude .git/"

# item stack support upload
lftp -c "set ftp:ssl-allow no; open -u $APIDOCS_USER,$APIDOCS_PASS andrei1058.com; mirror -Rev itemstack_common/target/apidocs ./$CI_PROJECT_TITLE  --ignore-time --parallel=10 --exclude-glob .git* --exclude .git/"

# material support upload
lftp -c "set ftp:ssl-allow no; open -u $APIDOCS_USER,$APIDOCS_PASS andrei1058.com; mirror -Rev material_version/target/apidocs ./$CI_PROJECT_TITLE  --ignore-time --parallel=10 --exclude-glob .git* --exclude .git/"

# particle support upload
lftp -c "set ftp:ssl-allow no; open -u $APIDOCS_USER,$APIDOCS_PASS andrei1058.com; mirror -Rev particle_common/target/apidocs ./$CI_PROJECT_TITLE  --ignore-time --parallel=10 --exclude-glob .git* --exclude .git/"

# sound support upload
lftp -c "set ftp:ssl-allow no; open -u $APIDOCS_USER,$APIDOCS_PASS andrei1058.com; mirror -Rev sounds_version/target/apidocs ./$CI_PROJECT_TITLE  --ignore-time --parallel=10 --exclude-glob .git* --exclude .git/"

# title support upload
lftp -c "set ftp:ssl-allow no; open -u $APIDOCS_USER,$APIDOCS_PASS andrei1058.com; mirror -Rev titlecommon/target/apidocs ./$CI_PROJECT_TITLE  --ignore-time --parallel=10 --exclude-glob .git* --exclude .git/"










