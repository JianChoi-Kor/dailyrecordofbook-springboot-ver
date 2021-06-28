// home slide 부분 js start
const slideContainer = document.querySelector('.top-slide-container')
const slide = document.querySelector('.slides')
const nextBtn = document.querySelector('#next-btn')
const prevBtn = document.querySelector('#prev-btn')
const interval = 10000


let slides = document.querySelectorAll('.slide')
let index = 1
let slideId

const firstClone = slides[0].cloneNode(true)
const lastClone = slides[slides.length - 1].cloneNode(true)

firstClone.id = 'first-clone'
lastClone.id = 'last-clone'

slide.append(firstClone)
slide.prepend(lastClone)


var slideWidth = slides[index].clientWidth

function resetSlideWidth() {
	slideWidth = slides[index].clientWidth
}


slide.style.transform = `translateX(${-slideWidth * index}px)`

console.log(slides)

const startSlide = () => {
    slideId = setInterval(() => {
        moveToNextSlide()
    }, interval)
}

const getSlides = () => document.querySelectorAll('.slide')

slide.addEventListener('transitionend', () => {
    slides = getSlides()
    if(slides[index].id === firstClone.id) {
        slide.style.transition = 'none'
        index = 1
        slide.style.transform = `translateX(${-slideWidth * index}px)`
    } 
    if(slides[index].id === lastClone.id) {
        slide.style.transition = 'none'
        index = slides.length - 2
        slide.style.transform = `translateX(${-slideWidth * index}px)`
    } 
})

const moveToNextSlide = () => {
    slides = getSlides()
    if(index >= slides.length -1) return
    index++
    slide.style.transform = `translateX(${-slideWidth * index}px)`
    slide.style.transition = '3.8s'
}

const moveToPreviousSlide = () => {
    if(index <= 0) return
    index--
    slide.style.transform = `translateX(${-slideWidth * index}px)`
    slide.style.transition = '3.8s'
}

slideContainer.addEventListener('mouseenter', () => {
    clearInterval(slideId)
})

slideContainer.addEventListener('mouseleave', startSlide)

nextBtn.addEventListener('click', moveToNextSlide)
prevBtn.addEventListener('click', moveToPreviousSlide)



startSlide()

window.addEventListener('resize', resetSlideWidth)

// home slide부분 js end





