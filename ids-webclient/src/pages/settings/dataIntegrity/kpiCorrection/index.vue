<template>
  <el-container>
    <el-header style="height: auto;padding: 0">
      <el-form ref="kpiCorrectionSearchForm" :model="kpiCorrectionSearchForm" :inline="true"
               style="float: left" :size="elementSize">

        <el-form-item :label="$t('kpiCor.selectPow')">
          <!--<el-input v-model="kpiCorrectionSearchForm.stationName" type="primary">
          </el-input>-->
          <StationInput
            :myValues="kpiCorrectionSearchForm.stationCode"
            :myTexts="kpiCorrectionSearchForm.stationName"
            @station-change="kpiCorrectionSearchFormStationChange"
            :isMutiSelect="false"></StationInput>
        </el-form-item>

        <el-form-item :label="$t('kpiCor.corMode')">
          <el-select v-model="kpiCorrectionSearchForm.correctionType" :placeholder="$t('kpiCor.choose')" style="width:155px">
            <el-option v-for="item in correctionTypes" :key="item.value" :label="item.label" :value="item.value">
            </el-option>
          </el-select>
        </el-form-item>

        <el-form-item>
          <el-button type="primary" :size="elementSize" icon="el-icon-search" @click="searchKpiCorrection">{{$t('kpiCor.search')}}</el-button>
        </el-form-item>

      </el-form>

      <el-form ref="btns" :inline="true"
               style="float: right" :size="elementSize">

        <el-form-item>
          <el-button type="primary" :size="elementSize" icon="el-icon-plus" @click="add">{{$t('kpiCor.add')}}</el-button>
        </el-form-item>

        <el-form-item>
          <el-button type="primary" :size="elementSize" icon="el-icon-edit" @click="modify">{{$t('kpiCor.edit')}}</el-button>
        </el-form-item>

        <el-form-item>
          <el-button type="primary" :size="elementSize" @click="synchronize">{{$t('kpiCor.syn')}}</el-button>
        </el-form-item>

      </el-form>
    </el-header>
    <el-main style="padding: 0px">

      <el-table :data="kpiCorrectionTable.list" class="kpiCorrectionTable" v-loading="kpiCorrectionTable.loading" :size="elementSize" ref="kpiCorrectionTable" border @row-click="rowClick" highlight-current-row>

        <el-table-column label="" width="60">
          <template slot-scope="scope">
            &nbsp;&nbsp;<el-radio v-model="getRadio" :label="scope.row.id">&nbsp;</el-radio>
          </template>
        </el-table-column>

        <el-table-column prop="stationName" :label="$t('kpiCor.plantName')" align="center">
        </el-table-column>

        <el-table-column prop="kpiKey" :label="$t('kpiCor.indexName')" align="center" :formatter="formatterKpiKey">
        </el-table-column>

        <el-table-column prop="reviseType" :label="$t('kpiCor.corMode')" align="center" :formatter="formatterReviseType">
        </el-table-column>

        <el-table-column prop="timeDim" :label="$t('kpiCor.timeDim')" align="center" :formatter="formatterTimeDim">
        </el-table-column>

        <el-table-column prop="replaceValue" :label="$t('kpiCor.repVal')" align="center">
        </el-table-column>

        <el-table-column prop="offsetValue" :label="$t('kpiCor.offset')" align="center">
        </el-table-column>

        <el-table-column prop="ratioValue" :label="$t('kpiCor.corFac')" align="center">
        </el-table-column>

        <el-table-column prop="reviseDate" :label="$t('kpiCor.data')" align="center">
          <template slot-scope="scope">
            <span>{{ scope.row.reviseDate && (new Date(scope.row.reviseDate).format( scope.row.timeDim === 'day' ? $t('dateFormat.yyyymmdd') : (scope.row.timeDim === 'month' ? $t('dateFormat.yyyymm') : $t('dateFormat.yyyy')) )) }}</span>
          </template>
        </el-table-column>

      </el-table>

      <el-pagination :current-page="kpiCorrectionTable.index" :page-sizes="kpiCorrectionTable.pageSizes" :page-size="kpiCorrectionTable.pageSize"
                     layout="total, sizes, prev, pager, next, jumper" :total="kpiCorrectionTable.count" style="float: right; margin-top: 5px;"
                     @size-change="val => {kpiCorrectionTable.pageSize = val; searchKpiCorrection()}" @current-change="val => {kpiCorrectionTable.index = val; searchKpiCorrection()}">
      </el-pagination>

      <el-dialog :title="addAndEditDialog.title" :visible.sync="addAndEditDialog.isVisible" width="40%">


        <el-form :model="addAndEditForm" ref="addAndEditForm" :size="elementSize" :rules="addAndEditForm.rules" class="addAndEditForm">
        <el-table :data="[{},{},{},{},{},{}]" ref="addAndEditDialogTable" border :show-header="false"
                  :highlight-current-row="false" class="addAndEditDialogTable"
                  :cell-style="cellStyle">

          <el-table-column prop="kpiName1" :label="$t('kpiCor.kpiName')" align="center" :width="150">
            <template slot-scope="scope">
              <div v-if="scope.$index === 0" class="self-is-required"><span>{{$t('kpiCor.plantName')}}</span></div>
              <div v-else-if="scope.$index === 1"><span>{{$t('kpiCor.indexName')}}</span></div>
              <div v-else-if="scope.$index === 2"><span>{{$t('kpiCor.corMode')}}</span></div>
              <div v-else-if="scope.$index === 3"><span>{{$t('kpiCor.dimension')}}</span></div>
              <div v-else-if="scope.$index === 4" class="self-is-required"><span>{{$t('kpiCor.corTime')}}</span></div>
              <div v-else-if="scope.$index === 5" class="self-is-required"><span>{{addAndEditForm.reviseName}}</span></div>
            </template>
          </el-table-column>

          <el-table-column prop="kpiValue1" :label="$t('kpiCor.kpiName')" align="left">
            <template slot-scope="scope">
              <el-form-item v-if="scope.$index === 0" style="margin-bottom: 0px" prop="stationName">
                <!--<el-input v-model="addAndEditForm.stationName"></el-input>-->
                <StationInput
                  :myValues="addAndEditForm.stationCode"
                  :myTexts="addAndEditForm.stationName"
                  @station-change="stationChange"
                  :isMutiSelect="false"></StationInput>
              </el-form-item>

              <el-form-item v-else-if="scope.$index === 1" style="margin-bottom: 0px" prop="kpiKey">
                <el-select v-model="kpiKey" :placeholder="$t('kpiCor.choose')" style="width:230px">
                  <el-option v-for="item in kpiKeys" :key="item.value" :label="item.label" :value="item.value">
                  </el-option>
                </el-select>
              </el-form-item>

              <el-form-item v-else-if="scope.$index === 2" prop="reviseType" style="margin-bottom: 0px">
                <el-select v-model="addAndEditForm.reviseType" :placeholder="$t('kpiCor.choose')" style="width:160px" @change="reviseTypeChange">
                  <el-option v-for="item in reviseTypes" :key="item.value" :label="item.label" :value="item.value" :disabled="item.disabled">
                  </el-option>
                </el-select>
              </el-form-item>

              <el-form-item v-else-if="scope.$index === 3" prop="timeDim" style="margin-bottom: 0px">
                <el-select v-model="addAndEditForm.timeDim" :placeholder="$t('kpiCor.choose')" style="width:120px" @change="timeDimChange">
                  <el-option v-for="item in timeDims" :key="item.value" :label="item.label" :value="item.value" :disabled="item.disabled">
                  </el-option>
                </el-select>
              </el-form-item>

              <el-form-item v-else-if="scope.$index === 4" prop="reviseDate" style="margin-bottom: 0px">
                <el-date-picker v-model="addAndEditForm.reviseDate" type="date" :placeholder="$t('kpiCor.modData')" style="width: 160px;"
                                :format="addAndEditForm.timeDim === 'day' ? $t('dateFormat.yyyymmdd') : (addAndEditForm.timeDim === 'month' ? $t('dateFormat.yyyymm') : $t('dateFormat.yyyy'))"
                                value-format="timestamp" :type="addAndEditForm.reviseDateType">
                </el-date-picker>
              </el-form-item>

              <el-form-item v-else-if="scope.$index === 5" prop="value" style="margin-bottom: 0px">
                <el-input v-model.number="addAndEditForm.value"></el-input>
              </el-form-item>

             <!-- <el-form-item v-else-if="scope.$index === 5 && addAndEditForm.reviseType == 'offsetMod'" prop="replaceValue" style="margin-bottom: 0px">
                <el-input v-model="addAndEditForm.offsetValue"></el-input>
              </el-form-item>

              <el-form-item v-else-if="scope.$index === 5 && addAndEditForm.reviseType == 'ratioMod'" prop="replaceValue" style="margin-bottom: 0px">
                <el-input v-model="addAndEditForm.ratioValue"></el-input>
              </el-form-item>

              <el-form-item v-else-if="scope.$index === 5 && addAndEditForm.reviseType == 'replaceMod'" prop="replaceValue" style="margin-bottom: 0px">
                <el-input v-model="addAndEditForm.replaceValue"></el-input>
              </el-form-item>-->

            </template>
          </el-table-column>

        </el-table>
        </el-form>

        <div class="dialog-footer" style="text-align: center;margin-top: 10px">
          <el-button type="primary" :size="elementSize" @click="save">{{$t('kpiCor.pre')}}</el-button>
          <el-button type="info" :size="elementSize" @click="cancel">{{$t('kpiCor.cancel')}}</el-button>
        </div>

      </el-dialog>

    </el-main>
  </el-container>
</template>
<script src='./index.js'></script>
<style lang='less' src='./index.less' scoped></style>
