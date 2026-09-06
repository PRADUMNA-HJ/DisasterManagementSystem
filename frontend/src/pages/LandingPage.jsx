import HeroScene from '../components/3d/HeroScene'

export default function LandingPage() {
  return (
    <div className="relative w-full h-screen overflow-hidden bg-background">
      {/* 3D Scene Layer */}
      <HeroScene />
      
      {/* UI Layer */}
      <div className="absolute inset-0 z-10 pointer-events-none flex flex-col justify-center items-center">
        <h1 className="text-white text-5xl font-bold tracking-tight mb-4">
          DISASTER MANAGEMENT SYSTEM
        </h1>
        <p className="text-gray-400 text-lg">
          One platform for awareness, response, and recovery.
        </p>
      </div>
    </div>
  )
}
