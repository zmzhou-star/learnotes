### 什么是 axios？
- Axios 是一个基于 promise 的 HTTP 库，可以用在浏览器和 node.js 中。
- 我们在使用Axios时候，一般会进行封装，主要包括全局的 axios 默认配置，请求拦截器，响应拦截器等。

### 特性
- 从浏览器中创建 XMLHttpRequests
- 从 node.js 创建 http 请求
- 支持 Promise API
- 拦截请求和响应
- 转换请求数据和响应数据
- 取消请求
- 自动转换 JSON 数据
- 客户端支持防御 CSRF/XSRF（Cross-site request forgery 跨站请求伪造）
### 安装
- 使用 npm:
```shell
$ npm install axios
```
- 使用 bower:
```shell
$ bower install axios
```
- 使用 yarn:
```shell
$ yarn add axios
```
- 使用 jsDelivr CDN:
```html
<script src="//cdn.jsdelivr.net/npm/axios/dist/axios.min.js"></script>
```
- 使用 unpkg CDN:
```html
<script src="//unpkg.com/axios/dist/axios.min.js"></script>
```
### 案例
- 执行 **GET** 请求
```javascript
// 为给定 ID 的 user 创建请求
axios.get('/user?ID=12345')
  .then(function (response) {
    console.log(response);
  })
  .catch(function (error) {
    console.log(error);
  });
// 上面的请求也可以这样做
axios.get('/user', {
    params: {
      ID: 12345
    }
  })
  .then(function (response) {
    console.log(response);
  })
  .catch(function (error) {
    console.log(error);
  });
```
- 执行 **POST** 请求
```javascript
axios.post('/user', {
firstName: 'Fred',
lastName: 'Flintstone'
})
.then(function (response) {
console.log(response);
})
.catch(function (error) {
console.log(error);
});
```
- 执行多个并发请求
```javascript
function getUserAccount() {
return axios.get('/user/12345');
}
function getUserPermissions() {
return axios.get('/user/12345/permissions');
}
axios.all([getUserAccount(), getUserPermissions()])
.then(axios.spread(function (acct, perms) {
// 两个请求现在都执行完成
}));
```
### 配置默认值
- 配置会以一个优先顺序进行合并。这个顺序是：在 lib/defaults.js 找到的库的默认值，然后是实例的 defaults 属性，最后是请求的 config 参数。后者将优先于前者。
#### 全局的 axios 默认配置
```javascript
axios.defaults.baseURL = 'https://api.example.com';
axios.defaults.headers.common['Authorization'] = AUTH_TOKEN;
axios.defaults.headers.post['Content-Type'] = 'application/x-www-form-urlencoded';
```
#### 自定义实例默认值
```javascript
const service = axios.create({
  crossDomain: true,
  headers: {
    'Content-Type': 'application/json;charset=utf-8'
  },
  baseURL: process.env.VUE_APP_BASE_API, // url = base url + request url
  withCredentials: true, // send cookies when cross-domain requests
  timeout: 60000 // request timeout
})
```
### 拦截器
在请求或响应被 then 或 catch 处理前拦截它们。
#### 添加请求拦截器
```javascript
service.interceptors.request.use(
  config => {
    // 在发送请求之前做些什么
    // do something before request is sent
    if (store.getters.token) {
      config.headers['Authorization'] = getToken()
    }
    return config
  },
  error => {
    // 对请求错误做些什么
    // do something with request error
    console.log('request error:', error) // for debug
    return Promise.reject(error)
  }
)
```

#### 添加响应拦截器
```javascript
service.interceptors.response.use(
  /**
   * Determine the request status by custom code
   * Here is just an example
   * You can also judge the status by HTTP Status Code
   */
  response => {
    // 对响应数据做点什么
    const res = response.data
    // if the custom code is not 20000, it is judged as an error.
    if (res.code !== 200) {
      Message({
        message: res.msg || 'Error',
        type: 'error',
        duration: 5 * 1000
      })
      // 50008: Illegal token; 50012: Other clients logged in; 50014: Token expired;
      if (res.code === 50008 || res.code === 50012 || res.code === 50014) {
        // to re-login
        MessageBox.confirm('You have been logged out, you can cancel to stay on this page, or log in again', 'Confirm logout', {
          confirmButtonText: 'Re-Login',
          cancelButtonText: 'Cancel',
          type: 'warning'
        }).then(() => {
          store.dispatch('user/resetToken').then(() => {
            location.reload()
          })
        })
      }
      return Promise.reject(new Error(res.msg || 'Error'))
    } else {
      // 直接返回数据层
      return res.data
    }
  },
  error => {
    // 对响应错误做点什么
    console.log('response error:', error) // for debug
    Message({
      message: error.message,
      type: 'error',
      duration: 5 * 1000
    })
    return Promise.reject(error)
  }
)
```
- 如果你想在稍后移除拦截器，可以这样：
```javascript
const myInterceptor = axios.interceptors.request.use(function () {/*...*/});
axios.interceptors.request.eject(myInterceptor);
```
- 可以为自定义 axios 实例添加拦截器
```javascript
const instance = axios.create();
instance.interceptors.request.use(function () {/*...*/});
```
### 使用实例
- 项目中使用实例：[https://gitee.com/zmzhou-star/easyboot/blob/master/vue-easyboot/src/utils/request.js](https://gitee.com/zmzhou-star/easyboot/blob/master/vue-easyboot/src/utils/request.js)

- 官方文档：[https://axios-http.com/zh/](https://axios-http.com/zh/)
- axios中文文档|axios中文网：[http://www.axios-js.com/zh-cn/](http://www.axios-js.com/zh-cn/)