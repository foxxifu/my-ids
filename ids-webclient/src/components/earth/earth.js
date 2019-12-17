import { Mesh, SphereGeometry, MeshPhongMaterial, Color } from 'three'
import { getTexture } from './common/utils'

export function createEarth () {
  return new Mesh(
    new SphereGeometry(5, 32, 32, Math.PI / -2),
    new MeshPhongMaterial({
      map: getTexture('earth'),
      bumpMap: getTexture('earthBump'),
      bumpScale: 0.15,
      specularMap: getTexture('earthSpec'),
      specular: new Color('#ffffff'),
      shininess: 5,
      transparent: true
    })
  )
}
