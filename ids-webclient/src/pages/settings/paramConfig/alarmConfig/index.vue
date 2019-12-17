 <template>
  <div id='alarmConfig' class="alarmConfig">
    <!-- 搜索栏 -->
    <div style="float: left">
      <el-form :inline="true" :model="searchData" class="demo-form-inline">
        <el-form-item :label="$t('alrCon.devType')">
          <el-select style="width: 300px" v-model="searchData.devTypeId" :placeholder="$t('alrCon.devType')" clearable>
            <el-option v-for="item in devTypes" :key="item" :value="item" :label="$t('devTypeId.' + item)"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item :label="$t('alrCon.alarmLevel')">
          <el-select style="width: 120px" v-model="searchData.severityId" :placeholder="$t('alrCon.allSelect')">
            <el-option v-for="item in alarmLevels" :key="item.value" :label="item.label" :value="item.value">
            </el-option>
          </el-select>
        </el-form-item>
        <el-form-item :label="$t('alrCon.alarmName')">
          <el-input style="width: 130px" v-model="searchData.alarmName" :placeholder="$t('alrCon.alarmName')" clearable></el-input>
        </el-form-item>
        <el-form-item :label="$t('alrCon.alarmCause')">
          <el-input style="width: 130px" v-model="searchData.alarmCause" :placeholder="$t('alrCon.alarmCause')" clearable></el-input>
        </el-form-item>
        <el-form-item :label="$t('alrCon.repairSuggestion')">
          <el-input style="width: 160px" v-model="searchData.repairSuggestion" :placeholder="$t('alrCon.repairSuggestion')" clearable></el-input>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="getAlarmConfigDatas(true)">{{ $t('search') }}</el-button>
        </el-form-item>
      </el-form>
    </div>
    <!-- 按钮栏 -->
    <div class="btns" style="float: right;margin-bottom: 10px;">
      <el-button @click="editAlarmConfig" type="primary">{{ $t('alrCon.edit') }}</el-button>
    </div>
    <!-- 数据列表栏 -->
    <div class="list-table">
      <el-table ref="singleTable" :data="list" @row-click="rowClick" highlight-current-row :size="resizeWidth +'px'"
                border :maxHeight="tableContentHeight">
        <el-table-column label="" width="60" align="center">
          <template slot-scope="scope">
            &nbsp;<el-radio v-model="getRadio" :label="scope.row.id">&nbsp;</el-radio>
          </template>
        </el-table-column>
        <el-table-column :label="$t('alrCon.stationName')" prop="stationName" align="center">
        </el-table-column>
        <el-table-column :label="$t('alrCon.devType')" prop="devTypeName" align="center">
          <template slot-scope="{row}">
            {{row.devTypeName ? $t('devTypeId.' + row.devTypeName) : ''}}
          </template>
        </el-table-column>
        <el-table-column :label="$t('alrCon.versionCode')" prop="versionCode" align="center">
        </el-table-column>
        <el-table-column :label="$t('alrCon.alarmName')" prop="alarmName" align="center">
        </el-table-column>
        <el-table-column :label="$t('alrCon.alarmType')" prop="alarmType" align="center">
          <template slot-scope="scope">
            <div>{{ getAlarmType(scope.row.alarmType) }}</div>
          </template>
        </el-table-column>
        <el-table-column :label="$t('alrCon.alarmLevel')" prop="alarmLevel" align="center">
          <template slot-scope="scope">
            <div>{{ getAlarmLevel(scope.row.alarmLevel) }}</div>
          </template>
        </el-table-column>
        <el-table-column :label="$t('alrCon.alarmCause')" :show-overflow-tooltip="true" prop="alarmCause" :width="celRealWidthArr[5]" align="center">
        </el-table-column>
        <el-table-column :label="$t('alrCon.repairSuggestion')" :show-overflow-tooltip="true" prop="repairSuggestion" :width="celRealWidthArr[6]" align="center">
        </el-table-column>
      </el-table>
      <div style="overflow: hidden;margin-top: 10px;">
        <div style="float: right;right: 10px;">
          <el-pagination @size-change="pageSizeChange" @current-change="pageChange" :current-page="searchData.page"
                         :page-sizes="[10, 20, 30, 50]" :page-size="searchData.pageSize"
                         layout="total, sizes, prev, pager, next, jumper" :total="total">
          </el-pagination>
        </div>
      </div>
      <!-- 弹出框 -->
      <el-dialog :title="getDialogTitle" :visible.sync="dialogVisible" width="40%" :before-close="closeDialog">

        <el-form :model="enterModel" ref="alarmConfigForm" :rules="rules" class="alarmConfigForm">
          <el-table :data="[{},{},{},{},{}]" ref="alarmConfigTable" border :show-header="false"
                    :cell-style="cellStyle" class="alarmConfigTable" :span-method="megerCells">
            <el-table-column align="center" :width="150">
              <template slot-scope="scope">
                <div v-if="scope.$index === 0"><span>{{ $t('alrCon.alarmName') }}</span></div>
                <div v-else-if="scope.$index === 1"><span>{{ $t('alrCon.alarmLevel') }}</span></div>
                <div v-else-if="scope.$index === 2"><span>{{ $t('alrCon.alarmType') }}</span></div>
                <div v-else-if="scope.$index === 3"><span>{{ $t('alrCon.alarmCause') }}</span></div>
                <div v-else-if="scope.$index === 4"><span>{{ $t('alrCon.repairSuggestion') }}</span></div>
              </template>
            </el-table-column>

            <el-table-column align="left">
              <template slot-scope="scope">
                <div v-if="scope.$index === 0">
                  <el-form-item prop="alarmName">
                    <el-input :disabled="true" v-model="enterModel.alarmName" :placeholder="$t('alrCon.alarmName')"></el-input>
                  </el-form-item>
                </div>
                <div v-else-if="scope.$index === 1">
                  <el-form-item prop="alarmLevel">
                    <el-select v-model="enterModel.alarmLevel" :placeholder="$t('alrCon.alarmLevel')">
                      <el-option v-for="item in alarmType" :key="item.value" :label="item.label" :value="item.value">
                      </el-option>
                    </el-select>
                  </el-form-item>
                </div>
                <div v-else-if="scope.$index === 2">
                  <el-form-item prop="alarmType">
                    <el-select v-model="enterModel.alarmType" :placeholder="$t('alrCon.alarmType')">
                      <el-option v-for="item in alarmTypes" :key="item.value" :label="item.label" :value="item.value">
                      </el-option>
                    </el-select>
                  </el-form-item>
                </div>
                <div v-else-if="scope.$index === 3">
                  <el-form-item prop="alarmCause">
                    <el-input type="textarea" v-model="enterModel.alarmCause" :placeholder="$t('alrCon.alarmCause')"></el-input>
                  </el-form-item>
                </div>
                <div v-else-if="scope.$index === 4">
                  <el-form-item prop="repairSuggestion">
                    <el-input type="textarea" v-model="enterModel.repairSuggestion" :placeholder="$t('alrCon.repairSuggestion')"></el-input>
                  </el-form-item>
                </div>
              </template>
            </el-table-column>

            <el-table-column align="center" :width="150">
              <template slot-scope="scope">
                <div v-if="scope.$index === 0"><span>{{ $t('alrCon.causeId') }}</span></div>
                <div v-else-if="scope.$index === 1"><span>{{ $t('alrCon.versionCode') }}</span></div>
              </template>
            </el-table-column>

            <el-table-column align="center">
              <template slot-scope="scope">
                <div v-if="scope.$index === 0">
                  <el-form-item prop="caseId">
                    <el-input v-model="enterModel.caseId" maxLength="11" :placeholder="$t('alrCon.causeId')" :disabled="true"></el-input>
                  </el-form-item>
                </div>
                <div v-else-if="scope.$index === 1">
                  <el-form-item prop="versionCode">
                    <el-input v-model="enterModel.versionCode" maxLength="11" :placeholder="$t('alrCon.versionCode')" :disabled="true"></el-input>
                  </el-form-item>
                </div>
              </template>
            </el-table-column>

          </el-table>

          <el-form-item class="tr-border" align="center" style="margin-top: 10px">
            <el-button type="primary" @click="submitForm">{{ $t('sure') }}</el-button>
            <el-button @click="closeDialog">{{ $t('cancel') }}</el-button>
          </el-form-item>

        </el-form>

      </el-dialog>
    </div>
  </div>
</template>
<script src="./index.js"></script>
<style lang='less' src='./index.less' scoped></style>
