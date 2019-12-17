# ids
Webpack 3 + Vue 2 + ElementUI 2 前端工程

## 构建步骤
``` bash
# 安装全局依赖
npm install -g webpack webpack-dev-server vue-cli

# 安装项目依赖
npm install

# 启动本地服务，用于与前端Mock数据测试 http://localhost:80
npm run test

# 启动本地服务，用于与后端接口联调 http://localhost:80
npm run dev

# 构建生产环境
npm run build


# 构建 demo 环境

# 1、构建 mock 数据，demo 数据存放在data目录下，构建的mock数据需要在z-config.js中配置路径结构
npm run mockData

# 2、构建 demo 系统
npm run demoBuild

# 以上两步可以合并为
npm run demo
```

## 工程目录结构
```text

```

## 模块的构成

#### vue 模板定义：
```vue
<template>
  <div>
    <!-- 内容区 -->
  </div>
</template>
<script src="./station.js"></script>
<style lang="less" src="./index.less" scoped></style>
```

#### 模块定义（ES6 语法）:
```js
// 输入
import { TOGGLE_SIDEBAR } from '@/store/mutation-types'

import ContentArea from '@/src/layout/children/content_area/index.vue'
// ...... 其他输入模块

export default {
  name: 'Layout',
  components: {
    ContentArea,
    // ...... 其他组件
  },
  data () { // 依赖数据
    return {
      sideBarOpen: true,
    }
  },
  computed: { // 计算
    showSidebar (vm) {
      return vm.$route.meta.showSidebar || false // 获取路由 meta 数据
    },
  },
  methods: { // 方法定义
    /**
     * 左侧菜单 收起/展开 切换
     */
    toggleSideBar () {
      this.sideBarOpen = !this.sideBarOpen // 数据变化
      this.$store.commit(TOGGLE_SIDEBAR, this.sideBarOpen) // 状态管理，提交状态变化
      setTimeout(window.onresize, 200);
    },

  },
}
```

#### 样式文件定义 （less 预编译）
```less
#modulesName {
 ...
}
```

## 国际化处理
采用 $t(), $tc(), $tl() 绑定国际化 key

##### 模板中:
```html
<p>{{$t('message.hello')}}</p>
```

##### v-text、v-html 中
```html
<p v-text="$t('message.hello')"></p>
```

##### 元素属性中
```html
<p :title="$t('message.hello')"></p>
```

##### js 中
```js
// this 为 Vue 实例
this.$t('message.hello')
```

## 主题变更
```bash
# 初始化主题
et -i

# 修改 element-veriables.scss 文件中相关配置，如主题色：
$--color-primary: #00397A !default;

# 执行修改并产生新的主题
et
```
