import * as Glow from './glow'
import { createAmbientLight, createSpotLight } from './lights'
import { createEarth } from './earth'
import { createCloud } from './cloud'
import { createLocationSprite, attachLabelToLocation, createLocationLine } from './locations'
import Stats from './stats'

const THREE = require('three')
const OrbitControls = require('three-orbit-controls')(THREE)
const EffectComposer = require('three-effectcomposer')(THREE)

const WIDTH = 925
const HEIGHT = 925

window.requestAnimationFrame = (function () {
  return window.requestAnimationFrame || window.webkitRequestAnimationFrame || window.mozRequestAnimationFrame || window.oRequestAnimationFrame || window.msRequestAnimationFrame ||
    function (callback) {
      window.setTimeout(callback, 1000 / 60)
    }
})()

export default class Earth {
  constructor (el, options) {
    this.container = typeof el === 'string' ? document.getElementById(el) : el
    this.options = options

    this.width = WIDTH * 2
    this.height = HEIGHT * 2
    this.earth = null
    this.camera = null
    this.renderer = null
    this.controller = null

    this.scene = null
    this.earthGroup = null
    this.locationGroup = null
    this.cloud = null
    this.hasGlow = false

    this.autoRotate = true
    this.rotationSpeed = 0.001
    this.cloudSpeed = -0.0003

    this.vector = (new THREE.Vector3(0, -1, 0)).normalize()

    this._init()
  }

  _init () {
    this._createRenderer()
    this._createScene()
    this._createCamera()
    this._createLight()
    this._createEarth()
    this._createCloud()
    // this._createLocations()
    // this._createOutGlow()
    this._createController()
    // this._createStats()

    this._loop()
  }

  _createController () {
    let controller = new OrbitControls(this.camera, this.container)
    controller.rotateSpeed = 0.2
    controller.autoRotate = false
    controller.enableZoom = false
    controller.enablePan = false
    controller.enabled = true

    controller.addEventListener('change', function (e) {
      let labels = document.querySelectorAll('.cesium-location-label')
      if (labels.length > 0) {
        for (let label of labels) {
          label.update()
        }
      }
    }, false)

    this.controller = controller
  }

  _createCamera () {
    let camera = new THREE.PerspectiveCamera(40, this.width / this.height, 0.1, 1000)
    camera.position.set(0, 10, -14)
    this.scene.add(camera)
    this.camera = camera
  }

  _createLight () {
    this.scene.add(createAmbientLight())
    this.camera.add(createSpotLight())
  }

  _createScene () {
    this.scene = new THREE.Scene()
    this.earthGroup = new THREE.Group()
    this.locationGroup = new THREE.Group()

    this.scene.add(this.earthGroup)
    this.earthGroup.add(this.locationGroup)
  }

  _createEarth () {
    let earth = createEarth()
    this.earthGroup.add(earth)
    this.earth = earth
  }

  _createCloud () {
    let cloud = createCloud()
    this.earthGroup.add(cloud)
    this.cloud = cloud
  }

  _createLocations () {
    const self = this
    let locations = self.options.locations
    locations.forEach(location => {
      let sprite = createLocationSprite(location)
      let line = createLocationLine(location)
      let label = attachLabelToLocation(location, self.camera, self.options.jump)
      self.locationGroup.add(sprite)
      self.locationGroup.add(line)
      self.container.appendChild(label)
    })
  }

  _createRenderer () {
    const self = this
    let renderer = new THREE.WebGLRenderer({
      alpha: true,
      // antialias: true,
      // preserveDrawingBuffer: true
    })
    let container = this.container

    renderer.setClearColor(0x000000, 0)
    renderer.setPixelRatio(window.devicePixelRatio) // this line would make FPS decreased at 30 for mobile device
    renderer.setSize(this.width, this.height)
    renderer.domElement.style.position = 'relative'
    renderer.domElement.style.width = this.width / 2 + 'px'
    renderer.domElement.style.height = this.height / 2 + 'px'

    let glow = document.createElement('div')
    glow.className = 'earth-backglow'

    container.appendChild(renderer.domElement)
    container.appendChild(glow)

    container.addEventListener('mouseover', function (e) {
      self.stopAutoRotate()
    })
    container.addEventListener('mouseout', function (e) {
      self.startAutoRotate()
    })

    this.renderer = renderer
  }

  _createStats () {
    let stats = new Stats()

    stats.setMode(0)

    stats.domElement.style.position = 'absolute'
    stats.domElement.style.left = '0px'
    stats.domElement.style.top = '0px'
    stats.domElement.style.display = 'none'

    this.stats = stats
    this.container.appendChild(stats.domElement)
  }

  _createOutGlow () {
    this.blurScene = new THREE.Scene()
    this.glowGroup = Glow.createOuterGlow()
    this.blurScene.add(this.glowGroup)

    let blurRenderTarget = new THREE.WebGLRenderTarget(this.width, this.height, {
      minFilter: THREE.LinearFilter,
      magFilter: THREE.LinearFilter,
      format: THREE.RGBAFormat,
      stencilBuffer: true
    })

    let blurRenderPass = new EffectComposer.RenderPass(this.blurScene, this.camera)
    let sceneRenderPass = new EffectComposer.RenderPass(this.scene, this.camera)

    this.blurComposer = new EffectComposer(this.renderer, blurRenderTarget)
    this.blurComposer.addPass(blurRenderPass)
    this.sceneComposer = new EffectComposer(this.renderer, blurRenderTarget)
    this.sceneComposer.addPass(sceneRenderPass)

    let effectBlend = new EffectComposer.ShaderPass(Glow.AdditiveBlendShader, 'tSampler1')
    effectBlend.uniforms['tSampler2'].value = this.blurComposer.renderTarget2.texture
    effectBlend.renderToScreen = true

    this.sceneComposer.addPass(effectBlend)
    this.hasGlow = true
  }

  _loop () {
    let isRunning = false;
    setInterval(_ => {
      if (!isRunning) {
        isRunning = true;
        if (this.renderer.domElement.offsetWidth) {
          this._animate();
          this._render();
        }
        isRunning = false;
      }
    }, 1000 / 24)
    // requestAnimationFrame(this._loop.bind(this))
    // this._animate()
    // this._render()
    // this.stats.update();
  }

  _animate () {
    let rotationSpeed = this.rotationSpeed
    let cloudSpeed = this.cloudSpeed

    if (this.autoRotate) {
      let x = this.camera.position.x
      let y = this.camera.position.y
      let z = this.camera.position.z

      // 相机向量（指向场景中心）
      // let v1 = new THREE.Vector3(x, y, z)

      // 求旋转轴，v1的垂直单位向量,令x=1,y=1,z=-(v1.x+v1.y)/v1.z
      let n = this.vector

      let sinDelta = Math.sin(rotationSpeed)
      let cosDelta = Math.cos(rotationSpeed)

      this.camera.position.x = x * (n.x * n.x * (1 - cosDelta) + cosDelta) + y * (n.x * n.y * (1 - cosDelta) - n.z * sinDelta) + z * (n.x * n.z * (1 - cosDelta) + n.y * sinDelta)
      this.camera.position.y = x * (n.x * n.y * (1 - cosDelta) + n.z * sinDelta) + y * (n.y * n.y * (1 - cosDelta) + cosDelta) + z * (n.y * n.z * (1 - cosDelta) - n.x * sinDelta)
      this.camera.position.z = x * (n.x * n.z * (1 - cosDelta) - n.y * sinDelta) + y * (n.y * n.z * (1 - cosDelta) + n.x * sinDelta) + z * (n.z * n.z * (1 - cosDelta) + cosDelta)

      // this.camera.position.x = this.camera.position.x * Math.cos(rotationSpeed) - this.camera.position.z * Math.sin(rotationSpeed)
      // this.camera.position.z = this.camera.position.z * Math.cos(rotationSpeed) + this.camera.position.x * Math.sin(rotationSpeed)
    }

    this.cloud.rotation.y += cloudSpeed
    this.controller.update()
  }

  _render () {
    if (this.isStart && this.hasGlow) {
      this.blurComposer.render()
      this.sceneComposer.render()
    } else {
      this.renderer.render(this.scene, this.camera)
      this.isStart = true
    }
  }

  setCamera () {
    if (arguments.length === 3) {
      this.camera.position.set(arguments[0], arguments[1], arguments[2])
    } else {
      this.camera.position.set(arguments[0].x, arguments[0].y, arguments[0].z)
    }
  }

  cameraPosition () {
    return {
      x: this.camera.position.x,
      y: this.camera.position.y,
      z: this.camera.position.z
    }
  }

  setLocations (locations) {
    const self = this
    if (locations && locations.length > 0) {
      self.earthGroup.remove(self.locationGroup)
      let labels = document.querySelectorAll('.cesium-location-label')
      if (labels.length > 0) {
        for (let label of labels) {
          self.container.removeChild(label)
        }
      }
      self.locationGroup = new THREE.Group()
      self.earthGroup.add(self.locationGroup)
      self.options.locations = locations
      locations.forEach(location => {
        let sprite = createLocationSprite(location)
        let line = createLocationLine(location)
        let label = attachLabelToLocation(location, self.camera, self.options.jump)
        self.locationGroup.add(sprite)
        self.locationGroup.add(line)
        self.container.appendChild(label)
      })
    }
  }

  startAutoRotate () {
    this.autoRotate = true
  }

  stopAutoRotate () {
    this.autoRotate = false
  }
}
