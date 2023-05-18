/**
 * Copyright 2012-2013 University Of Southern California
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package widesim.computation;

import org.cloudbus.cloudsim.UtilizationModelFull;

import java.util.ArrayList;
import java.util.List;


public class Job extends Task {

    private List<Task> taskList;

    public Job(
            final int jobId,
            final long jobLength,
            final String workflowName) {

        super(
                jobId,
                jobLength,
                1,
                0,    //TODO: take care of this 
                0,  //TODO: take care of this
                new UtilizationModelFull(),
                new UtilizationModelFull(),
                new UtilizationModelFull(),
                null,
                Double.MAX_VALUE,
                0,
                workflowName
            );

        this.taskList = new ArrayList<>();
    }

    public List<Task> getTaskList() {
        return this.taskList;
    }

    public void setTaskList(List<Task> list) {
        this.taskList = list;
    }

    public void addTaskList(List<Task> list) {
        this.taskList.addAll(list);
    }

}
