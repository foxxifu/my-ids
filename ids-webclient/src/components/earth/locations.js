import {
  SpriteMaterial,
  Sprite,
  LineBasicMaterial,
  Geometry,
  Vector3,
  Line,
  LineSegments,
  Color,
  Texture,
} from 'three'
import { getTexture } from './common/utils'

export function createLocationSprite (location) {
  let spriteMaterial = new SpriteMaterial({
    map: getTexture('markerPoint'),
    color: 0xffffff,
    fog: true
  })
  let sprite = new Sprite(spriteMaterial)
  sprite.userData = location
  sprite.castShadow = true

  let position = lonlat2xyz(location.position[1], location.position[0])
  sprite.position.set(position.x * 0.98, position.y * 0.98, position.z * 0.98)
  sprite.scale.set(0.3, 0.3, 0.3)

  return sprite
}

export function createLocationLabel (location) {
  const text = location.name
  const maxLen = 4
  const len = Math.min(text.length, maxLen)
  const fontSize = 60

  let canvas = document.createElement('canvas')
  let ctx = canvas.getContext('2d')
  ctx.save()
  ctx.fillStyle = '#ffffff'
  ctx.font = 'Bold ' + fontSize + 'px "Helvetica Neue", Helvetica, "PingFang SC", "Hiragino Sans GB", "Microsoft YaHei", Arial, sans-serif'
  ctx.fillText(text, (maxLen - len) * fontSize / 4, fontSize, (maxLen + 1) * fontSize)
  ctx.restore()
  let texture = new Texture(canvas)
  texture.needsUpdate = true

  let spriteMaterial = new SpriteMaterial({
    map: texture,
    color: 0xffffff,
    fog: true
  })
  let textObj = new Sprite(spriteMaterial)
  textObj.userData = location
  textObj.castShadow = true

  let position = lonlat2xyz(location.position[1], location.position[0])
  textObj.position.set(position.x + 0.15, position.y - 0.3, position.z - 0.2)
  textObj.scale.set(0.25 * len, 0.3, 0.4)

  return textObj
}

export function createLocationLine (location) {
  let material = new LineBasicMaterial({
    vertexColors: false,
    color: 0xFFFFFF
  })
  let geometry = new Geometry()
  let position = lonlat2xyz(location.position[1], location.position[0])
  geometry.vertices.push(
    new Vector3(position.x * 0.9, position.y * 0.9, position.z * 0.9),
    new Vector3(position.x * 0.96, position.y * 0.96, position.z * 0.96),
  )
  geometry.colors.push(
    new Color(0xFFFFFF),
    new Color(0x000000)
  )

  return new Line(geometry, material, LineSegments)
}

export function attachLabelToLocation (location, camera, callback) {
  const text = location.name

  let labelContainer = document.createElement('div')
  labelContainer.className = 'cesium-location-label'
  labelContainer.innerHTML = text
  labelContainer.title = text
  labelContainer.camera = camera
  let position = lonlat2xyz(location.position[1], location.position[0])

  labelContainer.setPosition = function (x, y, z) {
    this.style.left = x + 'px'
    this.style.top = y + 'px'
    this.style.zIndex = z
  }

  labelContainer.setVisible = function (vis) {
    this.style.display = !vis ? 'none' : 'block'
  }

  labelContainer.setSize = function (s) {
    let detailSize = Math.floor(8.4 - s * 2.5)
    this.style.fontSize = detailSize + 'pt'
    let totalHeight = detailSize * 2
    this.style.fontSize = totalHeight * 1.125 + 'pt'
    if (detailSize <= 8) {
      this.style.marginTop = '0px'
    } else {
      this.style.marginTop = '-1px'
    }
  }

  labelContainer.update = function () {
    const _this = this
    const screenXY = function (pos) {
      let worldVector = new Vector3(pos.x, pos.y, pos.z)
      let vector = worldVector.project(_this.camera)
      let halfWidth = 925 / 2
      let halfHeight = 925 / 2

      return {
        x: Math.round(vector.x * halfWidth + halfWidth),
        y: Math.round(-vector.y * halfHeight + halfHeight)
      }
    }

    let screenPos = screenXY(position)
    let s = 0.1 * Math.sqrt(Math.pow(position.x - _this.camera.position.x, 2) + Math.pow(position.y - _this.camera.position.y, 2) + Math.pow(position.z - _this.camera.position.z, 2))
    this.setSize(s)
    this.setVisible((screenPos.x > 80) && (screenPos.x < 850) && s < 1.56)

    let zIndex = Math.floor(1000 - s)
    if (this.hover) {
      zIndex = 1000
    }

    this.setPosition(screenPos.x, screenPos.y, zIndex)
  }

  const markerOver = function (e) {
    this.hover = true
    this.style.zIndex = 1000
  }
  const markerOut = function (e) {
    this.hover = false
    this.style.zIndex = 998
  }
  const markerClick = function (e) {
    callback && callback(location)
    e.stopPropagation()
    e.preventDefault()
  }

  labelContainer.addEventListener('mouseover', markerOver)
  labelContainer.addEventListener('mouseout', markerOut, true)
  labelContainer.addEventListener('click', markerClick, true)

  return labelContainer
}

/**
 * 经纬度转xyz
 * @param longitude 经度
 * @param latitude 纬度
 */
export function lonlat2xyz (longitude, latitude) {
  const radius = 5.4

  let lg = longitude * Math.PI / 180
  let lt = latitude * Math.PI / 180

  let y = radius * Math.sin(lt)
  let temp = radius * Math.cos(lt)
  let x = temp * Math.sin(lg)
  let z = temp * Math.cos(lg)

  return {x: x, y: y, z: z}
}
