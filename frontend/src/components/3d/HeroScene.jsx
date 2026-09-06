import { Canvas } from '@react-three/fiber'
import { Environment } from '@react-three/drei'

export default function HeroScene() {
  return (
    <div className="absolute inset-0 w-full h-screen z-0">
      <Canvas
        camera={{ position: [0, 0, 5], fov: 75 }}
        gl={{ antialias: true }}
      >
        <color attach="background" args={['#05070A']} />
        
        {/* Basic lighting for now */}
        <ambientLight intensity={0.2} />
        <directionalLight position={[10, 10, 5]} intensity={1} />
        
        {/* Placeholder mesh */}
        <mesh>
          <boxGeometry args={[1, 1, 1]} />
          <meshStandardMaterial color="#333333" wireframe />
        </mesh>
        
        <Environment preset="city" />
      </Canvas>
    </div>
  )
}
