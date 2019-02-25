#!/usr/bin/env bash
export LANG=en_US.UTF-8
export PATH=/usr/local/sbin:/usr/local/bin:/sbin:/bin:/usr/sbin:/usr/bin

jar_name="skeleton.jar"
pid_file_name="skeleton.pid"
shell_path=$(cd `dirname $0`; pwd)
base_path=`dirname ${shell_path}`
cd ${base_path}

function app_status() {
    if [ ! -f "$pid_file_name" ]; then
        return 1
    fi
    pid=`cat ${pid_file_name}`
    process=`ps -p ${pid}`
    if [ -n "${process}" ];then
        return 0
    else
        return 1
    fi
}

function status_info() {
    app_status
	if [ $? -eq 0 ]; then
		echo "skeleton is running."
	else
		echo "skeleton is not running."
	fi
}

function start_app() {
    app_status
	if [ $? -eq 0 ]; then
       echo "[INFO] The program is running. No need to start it again."
       return 0
    fi
    nohup java -jar ./app/${jar_name} >> /dev/null 2>&1 &
}

function restart_app() {
    app_status
    if [ $? -eq 0 ]; then
       echo "[INFO] The program is running, stop it."
       stop_app
    fi
    start_app
}


function stop_app() {
    app_status
	if [ $? -eq 1 ]; then
       echo "[INFO] The program is not running."
       return 0
    fi

    pid=`cat ${pid_file_name}`
    kill ${pid}
    echo "[INFO] stop success"
}

case "$1" in
        'start')
                start_app
        ;;
        'stop')
                stop_app
        ;;
        'restart')
            restart_app
        ;;
        'status')
                status_info
        ;;
        *)
            echo "Usage: $0 {start|stop|restart|status|}"
esac
