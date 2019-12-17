/**
 * 天气预报相关接口
 */
import fetch from '@/utils/fetch'

/**
 * 获取实时天气信息
 * @param data
 * <pre>
 *   location: '纬度:经度', <br>
 *   language: '语言' <br>
 * </pre>
 */
const getWeatherNow = (data = {
  location: '0:0',
  language: 'zh_cn'
}) => fetch('/biz/weather/getWeatherNow', data, 'POST')

/**
 * 逐日天气预报
 * @param data
 * <pre>
 *     days: 3, // '天气的天数默认3天' <br>
 *     language: '语言', <br>
 *     location: '必填，维度:经度', <br>
 *     start: '开始天数，默认0' <br>
 * </pre>
 */
const getWeatherDaily = (data = {
  days: 3,
  location: '0:0',
  start: 0,
  language: 'zh_cn'
}) => fetch('/biz/weather/getWeatherDaily', data, 'POST')

export default {
  getWeatherNow,
  getWeatherDaily,
}
