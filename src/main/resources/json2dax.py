import sys
import json


def json2dax(json_file, dax_file):
    with open(json_file) as f:
        data = json.load(f)
    workflows = data['workflows']
    workflow = workflows[0]
    workflow_id = workflow['workflow_id']
    tasks = workflow['tasks']
    files = set()
    child_count = 0
    # for task in tasks:
    #     child_count += len(task['children'])
    #     for file in task['input_files']:
    #         files.add(file["file_id"])
    #     for file in task['output_files']:
    #         files.add(file["file_id"])
    with open(dax_file, 'w') as f:
        f.write('<?xml version="1.0" encoding="UTF-8"?>\n')
        f.write(
            f'<adag xmlns="http://pegasus.isi.edu/schema/DAX" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xsi:schemaLocation="http://pegasus.isi.edu/schema/DAX http://pegasus.isi.edu/schema/dax-2.1.xsd" version="2.1" count="1" index="0" name="{workflow_id}" jobCount="{len(tasks)}" fileCount="0" childCount="{child_count}">\n')
        for task in tasks:
            task_id = task['task_id']
            runtime = task['runtime']
            input_files = task['input_files']
            output_files = task['output_files']
            children = task['children']
            output_file_size = task['output_file_size']
            f.write(
                f'<job id="{task_id}" namespace="{workflow_id}" name="{workflow_id}:{task_id}" version="1.0" runtime="{runtime}">\n')
            for file in input_files:
                file_id = file['file_id']
                size = file['size']
                f.write(
                    f'<uses file="{file_id}" link="input" register="true" transfer="true" optional="false" type="data" size="{size}"/>\n')
            for file in output_files:
                file_id = file['file_id']
                size = file['size']
                f.write(
                    f'<uses file="{file_id}" link="output" register="true" transfer="true" optional="false" type="data" size="{size}"/>\n')
            f.write('</job>\n')
        for task in tasks:
            task_id = task['task_id']
            children = task['children']
            for child in children:
                f.write(f'<child ref="{child}">\n')
                f.write(f'<parent ref="{task_id}"/>\n')
                f.write('</child>\n')
        f.write('</adag>\n')


if __name__ == '__main__':
    json_file = sys.argv[1]
    dax_file = sys.argv[2]
    json2dax(json_file, dax_file)
