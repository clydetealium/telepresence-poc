#!/usr/bin/env bash
set -e
# Next line sets the -x or +x flag for any script which sources logging.sh
test "${DEBUG_VERBOSE}" = "true" && set -x || set +x

# Log a basic message with calling function and date prefixed
log_message() {
  local parent="${0:-bash}"
  echo "[$(basename ${parent})] - $@"
}
export -f log_message

# Used to obtain the color code from a passed in color without
# an associative array for portability
# https://stackoverflow.com/questions/16553089/dynamic-variable-names-in-bash
get_color() {
  local color=$(echo ${1} | tr '[:lower:]' '[:upper:]')
  JENKINS_COLOR_BLACK='\e[30m'
  JENKINS_COLOR_RED='\e[31m'
  JENKINS_COLOR_GREEN='\e[32m'
  JENKINS_COLOR_YELLOW='\e[33m'
  JENKINS_COLOR_BLUE='\e[34m'
  JENKINS_COLOR_MAGENTA='\e[35m'
  JENKINS_COLOR_CYAN='\e[36m'
  JENKINS_COLOR_WHITE='\e[37m'
  JENKINS_COLOR_GRAY='\e[90m'
  JENKINS_COLOR_BOLD='\e[1m'
  JENKINS_COLOR_RESET='\e[0m'
  JENKINS_COLOR_GREY=${JENKINS_COLOR_GRAY}
  desired_color=JENKINS_COLOR_${color}
  echo ${!desired_color}
}
export -f get_color

# Get the color reset code
get_color_reset() {
  echo $(get_color reset)
}
export -f get_color_reset

# Wrapper for log_message, with bolded color!
log_messagec() {
  local parent="${0:-bash}"
  local color="${1}" && shift
  echo -e "$(get_color bold)$(get_color ${color})[$(basename ${parent})] - $@ $(get_color_reset)"
}
export -f log_messagec

# Calls log_messagec function only when DEBUG flag is true
log_debugc() {
  local color="${1}" && shift
  test ! "${DEBUG}" = "true" || log_messagec ${color} $@
}
export -f log_debugc

# Calls log_debugc with cyan color only when DEBUG flag is true
log_debug() {
  log_debugc cyan $@
}
export -f log_debug

# Calls log_messagec with red color and then exits
msg_and_exit() {
  local exit_val=${1}
  local message=${2}
  log_messagec red "ERROR: ${message}" >>/dev/stderr
  exit ${exit_val}
}
export -f msg_and_exit

# Echoes a list or stringlist in the format [item1 | item2 | ...]
# Examples:
# to_check=(item1 item2); LIST=${to_check[@]} pretty_print_list
# to_check='item1,item2'; LIST=${to_check} DELIMITER=, pretty_print_list
pretty_print_list() {
  local delim=${DELIMITER:-' '}
  local list=${LIST}
  test -x $(command -v sed) || msg_and_exit 1 "pretty_print_list(): sed command must be installed to pretty print list."
  echo "[${list}]" | sed "s~${delim}~ | ~g"
}
export -f pretty_print_list