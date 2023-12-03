#!/usr/bin/expect

set u [lindex $argv 0]
set t [lindex $argv 1]

spawn git push -u origin main
expect "Username for 'https://github.com':"
send "$1\r"

expect "Password for 'https://$1@github.com':"
send "$2\r"

interact
