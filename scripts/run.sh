gradle_command=$1
job_name=$2
output=$(./gradlew $gradle_command)

exit_code=$?
if [ $exit_code -eq 1 ]; then
    scan_link=$(echo "$output" | grep -o 'https://gradle.com/s/[^ ]*')
    build_scan_link=$(echo "$scan_link" | awk '{print $1}')
    $(./scripts/publish_report.sh "$job_name" "$build_scan_link" > /dev/null) 
    echo "REPORT LINK: " "$build_scan_link"
    exit 1
else
    echo "$output"
fi
