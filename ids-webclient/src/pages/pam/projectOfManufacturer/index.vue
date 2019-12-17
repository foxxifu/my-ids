<template>
  <el-container style="height: 100%;" id="projectOfManufacturer">
    <el-aside style="width: 236px;border-right: 1px solid #8080804d;padding-right: 5px;">

      <el-form ref="projectNameSearchForm" :model="projectNameSearchForm" :inline="true" :size="elementSize" style="border-bottom: 1px solid #8080804d;margin-bottom: 5px">

        <el-form-item style="margin-bottom: 5px;margin-right: 0px;">
          <el-input placeholder="输入项目名称" v-model="projectNameSearchForm.projectName" class="input-with-select">
            <el-button slot="append" icon="el-icon-search"></el-button>
          </el-input>
        </el-form-item>

      </el-form>

      <el-tree :data="data" :props="defaultProps" @node-click="handleNodeClick" style="background: transparent;" :default-expanded-keys="['0', '1', '2', '3', '4', '5', '6', '7']" node-key="id"></el-tree>

    </el-aside>

    <el-container>
      <el-header style="height: auto">
        <el-form ref="projectSearchForm" :model="projectSearchForm" :inline="true"
                 style="height: 40px;float: left" :size="elementSize">
          <el-form-item>
            <el-input v-model="projectSearchForm.name" placeholder="输入项目名称"></el-input>
          </el-form-item>

          <el-form-item>
            <el-select v-model="projectSearchForm.status" placeholder="项目状态" style="width: 130px">
              <el-option v-for="item in options" :key="item.value" :label="item.label" :value="item.value">
              </el-option>
            </el-select>
          </el-form-item>

          <el-form-item>
            <el-button type="primary" :size="elementSize" icon="el-icon-search">搜索</el-button>
          </el-form-item>
        </el-form>

        <el-form ref="btnsForm" :inline="true" label-width="50px" style="float:right;height: 40px" :size="elementSize">

          <el-form-item>
            <el-button type="primary" icon="el-icon-plus">新建项目</el-button>
          </el-form-item>

        </el-form>

      </el-header>

      <el-main style="padding-top: 5px">
        <el-table :data="tableData" border :size="elementSize" @select="select" :fit="true">

          <el-table-column type="expand">
            <template slot-scope="props">

              <el-tabs value="earlyPeriod" tab-position="top" style="height: calc(100% - 0px); width: 100%;">
                <el-tab-pane label="项目前期" name="earlyPeriod" style="height: 100%">
                  <el-table :data="props.row.earlyPeriod" border :size="elementSize" @select="select" max-height="600" :fit="true">

                    <el-table-column prop="num" label="任务编号" align="center">
                    </el-table-column>

                    <el-table-column prop="name" label="任务名称" align="center">
                    </el-table-column>

                    <el-table-column prop="taskProgress" label="任务进度" align="center">
                      <template slot-scope="scope">
                        <el-progress :text-inside="true" :stroke-width="18" :percentage="scope.row.taskProgress"></el-progress>
                      </template>
                    </el-table-column>

                    <el-table-column prop="involvingDepartment" label="涉及部门" align="center">
                    </el-table-column>

                    <el-table-column prop="beginTime" label="开始时间" align="center" :width="160">
                    </el-table-column>

                    <el-table-column prop="endTime" label="结束时间" align="center" :width="160">
                    </el-table-column>

                    <el-table-column prop="taskStatus" label="任务状态" align="center">
                    </el-table-column>

                    <el-table-column prop="taskLeader" label="任务负责人" align="center">
                    </el-table-column>

                    <el-table-column prop="operating" label="操作" align="center">
                      <template slot-scope="scope">
                        <el-button @click="operating(scope.$index, tableData)" type="text">操作</el-button>
                      </template>
                    </el-table-column>

                  </el-table>
                </el-tab-pane>
                <el-tab-pane label="项目开发期" name="developmentPeriod" style="height: 100%">
                  <el-table :data="props.row.developmentPeriod" border :size="elementSize" @select="select" max-height="600" :fit="true">

                    <el-table-column prop="num" label="任务编号" align="center">
                    </el-table-column>

                    <el-table-column prop="name" label="任务名称" align="center">
                    </el-table-column>

                    <el-table-column prop="taskProgress" label="任务进度" align="center">
                      <template slot-scope="scope">
                        <el-progress :text-inside="true" :stroke-width="18" :percentage="scope.row.taskProgress"></el-progress>
                      </template>
                    </el-table-column>

                    <el-table-column prop="involvingDepartment" label="涉及部门" align="center">
                    </el-table-column>

                    <el-table-column prop="beginTime" label="开始时间" align="center" :width="160">
                    </el-table-column>

                    <el-table-column prop="endTime" label="结束时间" align="center" :width="160">
                    </el-table-column>

                    <el-table-column prop="taskStatus" label="任务状态" align="center">
                    </el-table-column>

                    <el-table-column prop="taskLeader" label="任务负责人" align="center">
                    </el-table-column>

                    <el-table-column prop="operating" label="操作" align="center">
                      <template slot-scope="scope">
                        <el-button @click="operating(scope.$index, tableData)" type="text">操作</el-button>
                      </template>
                    </el-table-column>

                  </el-table>
                </el-tab-pane>
                <el-tab-pane label="项目建设期" name="buidingPeriod" style="height: 100%">
                  <el-table :data="props.row.buidingPeriod" border :size="elementSize" @select="select" max-height="600" :fit="true">

                    <el-table-column prop="num" label="任务编号" align="center">
                    </el-table-column>

                    <el-table-column prop="name" label="任务名称" align="center">
                    </el-table-column>

                    <el-table-column prop="taskProgress" label="任务进度" align="center">
                      <template slot-scope="scope">
                        <el-progress :text-inside="true" :stroke-width="18" :percentage="scope.row.taskProgress"></el-progress>
                      </template>
                    </el-table-column>

                    <el-table-column prop="involvingDepartment" label="涉及部门" align="center">
                    </el-table-column>

                    <el-table-column prop="beginTime" label="开始时间" align="center" :width="160">
                    </el-table-column>

                    <el-table-column prop="endTime" label="结束时间" align="center" :width="160">
                    </el-table-column>

                    <el-table-column prop="taskStatus" label="任务状态" align="center">
                    </el-table-column>

                    <el-table-column prop="taskLeader" label="任务负责人" align="center">
                    </el-table-column>

                    <el-table-column prop="operating" label="操作" align="center">
                      <template slot-scope="scope">
                        <el-button @click="operating(scope.$index, tableData)" type="text">操作</el-button>
                      </template>
                    </el-table-column>

                  </el-table>
                </el-tab-pane>
                <el-tab-pane label="并网调试期" name="onGridPeriod" style="height: 100%">
                  <el-table :data="props.row.onGridPeriod" border :size="elementSize" @select="select" max-height="600" :fit="true">

                    <el-table-column prop="num" label="任务编号" align="center">
                    </el-table-column>

                    <el-table-column prop="name" label="任务名称" align="center">
                    </el-table-column>

                    <el-table-column prop="taskProgress" label="任务进度" align="center">
                      <template slot-scope="scope">
                        <el-progress :text-inside="true" :stroke-width="18" :percentage="scope.row.taskProgress"></el-progress>
                      </template>
                    </el-table-column>

                    <el-table-column prop="involvingDepartment" label="涉及部门" align="center">
                    </el-table-column>

                    <el-table-column prop="beginTime" label="开始时间" align="center" :width="160">
                    </el-table-column>

                    <el-table-column prop="endTime" label="结束时间" align="center" :width="160">
                    </el-table-column>

                    <el-table-column prop="taskStatus" label="任务状态" align="center">
                    </el-table-column>

                    <el-table-column prop="taskLeader" label="任务负责人" align="center">
                    </el-table-column>

                    <el-table-column prop="operating" label="操作" align="center">
                      <template slot-scope="scope">
                        <el-button @click="operating(scope.$index, tableData)" type="text">操作</el-button>
                      </template>
                    </el-table-column>

                  </el-table>
                </el-tab-pane>
              </el-tabs>

            </template>
          </el-table-column>


          <el-table-column prop="num" label="编号" width="60" align="center">
            <template slot-scope="scope">
              <el-button @click="viewRow(scope.$index, tableData)" type="text" size="small"> {{scope.row.num}}
              </el-button>
            </template>
          </el-table-column>

          <el-table-column prop="projectPic" label="项目图片" align="center" :width="200">
            <template slot-scope="scope">
              <div class="projectPic"></div>
            </template>
          </el-table-column>

          <el-table-column prop="name" label="项目名称" align="center">
          </el-table-column>

          <el-table-column prop="schedule" label="项目进度" align="center" :width="200">
            <template slot-scope="scope">
              <el-progress type="circle" :percentage="scope.row.schedule"></el-progress>
            </template>
          </el-table-column>

          <el-table-column prop="beginTime" label="开始时间" align="center" :width="160">
          </el-table-column>

          <el-table-column prop="endTime" label="截止时间" align="center" :width="160">
          </el-table-column>

          <el-table-column prop="status" label="项目状态" align="center">
          </el-table-column>

          <el-table-column prop="charge" label="负责人" align="center">
          </el-table-column>

          <el-table-column label="操作" align="center">
            <template slot-scope="scope">
              <el-button @click="editRow(scope.$index, tableData)" type="text">操作</el-button>
            </template>
          </el-table-column>

        </el-table>

        <el-pagination :current-page="1" :page-sizes="[10, 20, 30, 50]" :page-size="10"
                       layout="total, sizes, prev, pager, next, jumper" :total="2" style="float: right; margin-top: 5px;">
        </el-pagination>

      </el-main>

    </el-container>

  </el-container>
</template>
<style lang='less' src='./index.less' scoped></style>
<script src='./index.js'></script>


