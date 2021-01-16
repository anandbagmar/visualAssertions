#! /bin/sh

run() {
  echo "run $1"
  startTime=$(date -j -f "%Y-%m-%d" "2010-05-15" "+%s")
  echo "starting: " $startTime
  ./gradlew clean test > eyes-$1-$startTime.log
  finishTime=$(date -j -f "%Y-%m-%d" "2010-05-15" "+%s")
  echo "finished: " $finishTime
  echo "time taken for run $1:" $(($finishTime - $startTime)) "seconds"
  echo ""
}

run 1




