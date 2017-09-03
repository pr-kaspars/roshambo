class Shape {

  constructor(name, value) {
    if (!name || !value) {
      return;
    }

    this.value = value;
    this.name = name;

    this.onclick = () => {};
    this.imageTag = new Image();
    this.imageTag.src = `/img/${name}.png`.toLowerCase();

    this.element = document.createElement('div');
    this.element.appendChild(this.imageTag);
    this.element.className = 'shape';
    this.element.onclick = event => {
      this.selected = !this.selected;
      this.onclick();
    };
  }

  get selected() {
    return this.element.className.indexOf('selected') !== -1;
  }

  set selected(selected) {
    this.element.className = (selected) ? 'shape selected' : 'shape';
  }

  set position(pos) {
    this.element.style.left = pos.x;
    this.element.style.top = pos.y;
  }

  notify(event) {
    if (event.target === this) {
      return;
    }

    switch (event.name) {
      case 'selected':
        this.selected = false;
        return;
    }
  }

  hide() {
    this.element.style.display = 'none';
  }

  show() {
    this.element.style.display = 'inline-block';
  }
}

class Carousel {
  constructor(container) {
    this.shapes = [];
    this.container = container;
    this.subscribers = [];
  }

  addShape(shape) {
    this.container.appendChild(shape.element);
    this.shapes.push(shape);
    this.register(shape);
    shape.onclick = () => this.notifySubscribers({name: 'selected', target: shape});
  }

  addAllShapes(shapes) {
    shapes.forEach(shape => this.addShape(shape));
  }

  notifySubscribers(event) {
    this.subscribers.forEach(subscriber => subscriber.notify(event));
  }

  register(observer) {
    this.subscribers.push(observer);
  }

  unregister(observer) {
    var i = this.subscribers.indexOf(observer);
    if (i === -1) {
      return;
    }

    this.subscribers.splice(i);
  }

  animateCompare(results, callback) {
    let n = this.shapes.length;
    let w = this.container.offsetWidth / 2 - 50;
    let h = this.container.offsetHeight / 2 - 50;
    let l = Math.min(w, h);
    let participants = results.slice(1);

    let sideSlide = (t, d, e) => {
      n = results.slice(1);
      setTimeout(() => {
        let r = e.element.getBoundingClientRect();

        if (Math.sqrt(Math.pow(w - r.left, 2) + Math.pow(h - r.top, 2)) > l) {
          if (--n !== 0) {
            return callback();
          }

          return;
        }

        e.position = {x: r.left + d.x, y: r.top + d.y};
        sideSlide(t, d, e);
      }, t);
    };

    let completeCb = () => {
      if (--n !== 0) {
        return;
      }

      this.shapes
        .map(shape => {shape.selected = false; return shape;})
        .filter(shape => participants.indexOf(shape.value) === -1)
        .forEach(shape => shape.hide());

      if (results[0] === 0) {
        let shape = this.shapes.find(shape => shape.value === results[1]);
        shape.element.style.width = 200;
        shape.element.style.height = 200;
        shape.element.style.top = h - 50;
        shape.element.style.left = w - 50;
        callback();
        return;
      }

      this.shapes
        .filter(shape => participants.indexOf(shape.value) !== -1)
        .forEach(shape => {
          let i = participants.indexOf(shape.value);
          if (i === -1) {
            return;
          }
          participants[i] = null;
          return sideSlide(10, {x: 2 * (1 - (i % 2) * 2), y: 0}, shape);
        });
    };

    let animate = (t, d, e) => {
      setTimeout(() => {
        let r = e.element.getBoundingClientRect();

        if (Math.sqrt(Math.pow(w - r.left, 2) + Math.pow(h - r.top, 2)) < 3) {
          return completeCb();
        }

        e.position = {x: r.left - d.x, y: r.top - d.y};
        animate(t, d, e);
      }, t);
    };

    this.shapes.forEach((e, i, a) => {
        let rect = e.element.getBoundingClientRect();
        animate(50, {x: (rect.left - w) / 10, y: (rect.top - h) / 10}, e);
      });
  }

  animateEntry() {
    this.subscribers.forEach(subscriber => subscriber.notify('start'));

    let n = this.shapes.length;
    let w = this.container.offsetWidth / 2 - 50;
    let h = this.container.offsetHeight / 2 - 50;
    let l = Math.min(w, h);

    /**
     * @param t {Integer} the timeout between frames.
     * @param a {Float} the stating position in radians.
     * @param d {Float} the rotation per frame in radians.
     * @param g {Float} the max rotation radians.
     * @param e {Node}  the element.
     * @param c {Function} the callback.
     */
    let animate = (t, a, d, g, e, c) => {
      setTimeout(() => {
        if (a > g) {
          return c(a);
        }

        e.position = { x: l * Math.cos(a) + w, y: l * Math.sin(a) + h };

        animate(t, a + d, d, g, e, c);
      }, t);
    };

    let completeCb = (a) => {
      if (--n !== 0) {
        return;
      }

      this.subscribers.forEach(subscriber => subscriber.notify('complete'));
    };

    this.shapes.map((e, i, a) => [
        12 / (i + 1),
        -0.5 * Math.PI,
        Math.PI / 180,
        Math.PI * 2 * i / a.length,
        e,
        completeCb
      ])
      .forEach(e => setTimeout(() => animate.apply(this, e), 10));
  }
}

class Game {
  constructor(body) {
    this.body = body;
    this.participantSelector();
  }

  participantSelector() {
    this.radioHuman = document.createElement('input');
    this.radioHuman.setAttribute('type', 'radio');
    this.radioHuman.setAttribute('name', 'participant');
    this.labelHuman = document.createElement('label');
    this.labelHuman.appendChild(this.radioHuman);
    this.labelHuman.appendChild(document.createTextNode(' Human'));

    this.radioAndroid = document.createElement('input');
    this.radioAndroid.setAttribute('type', 'radio');
    this.radioAndroid.setAttribute('name', 'participant');
    this.labelAndroid = document.createElement('label');
    this.labelAndroid.appendChild(this.radioAndroid);
    this.labelAndroid.appendChild(document.createTextNode(' Android'));

    this.selectorElem = document.createElement('div');
    this.selectorElem.className = 'participant-selector';
    this.selectorElem.appendChild(this.labelHuman);
    this.selectorElem.appendChild(document.createElement('br'));
    this.selectorElem.appendChild(this.labelAndroid);

    this.body.appendChild(this.selectorElem);
  }

  create() {
    this.carousel.animateEntry();
    this.carousel.register(this);
  }

  set button(button) {
    button.onclick = (event) => this.playCallback();
    super.button = button;
  }

  playCallback() {
    this.button.hide();
    let shape = this.carousel.shapes.find(shape => shape.selected);
    fetch(`/game?human=true&shapeValue=${shape.value}`, {method: 'POST'})
      .then(response => response.text())
      .then(text => text.split("\n").map(s => s.trim()).filter(s => !!s).map(s => parseInt(s)))
      .then(rez => this.carousel.animateCompare(rez, () => this.showResult(rez)));
  }

  showResult(results) {
    let gameResultText = null;
    switch (results[0]) {
      case 0:
        gameResultText = 'Draw!';
        break;
      default:
        gameResultText = `${this.carousel.shapes.find(shape => shape.value === results[0]).name} wins!`;
        break;
    }


    this.gameResultElement = document.createElement('h2');
    this.gameResultElement.appendChild(document.createTextNode(gameResultText))
    this.body.appendChild(this.gameResultElement);

    this.resetButtonElem = document.createElement('button');
    this.resetButtonElem.className = 'reset';
    this.resetButtonElem.appendChild(document.createTextNode('Play again'))
    this.body.appendChild(this.resetButtonElem);
  }

  notify(event) {
    console.log(event.name);
    switch (event.name) {
      case 'selected':
        let fn = this.carousel.shapes.find(shape => shape.selected) ? 'show' : 'hide';
        return this.button[fn].call(this.button);
      case 'start':
        return this.button.hide();
      case 'complete':
        return this.carousel.shapes.forEach(shape => shape.element.className = 'shape');
    }
  }
}

class PlayButton {
  constructor(text) {
    this.buttonElem = document.createElement('button');
    this.buttonText = document.createTextNode(text);
    this.buttonElem.appendChild(this.buttonText);
  }

  set onclick(callback) {
    this.buttonElem.onclick = callback;
  }

  hide() {
    this.buttonElem.style.display = 'none';
  }

  show() {
    this.buttonElem.style.display = 'inline-block';
  }
}

function main() {
  var button = new PlayButton('Play');
  button.hide();

  var body = document.getElementsByTagName('body')[0];
  body.appendChild(button.buttonElem);

  var carousel = new Carousel(body);

  var game = new Game(body);
  game.button = button;
  game.carousel = carousel;

  fetch('/shapes')
    .then(response => response.text())
    .then(text => {
      let shapes = text.split("\n")
        .filter(line => !!line)
        .map(line => line.split(/\s+/))
        .map(arr => new Shape(arr[0], parseInt(arr[1])))
        .sort(() => .5 - Math.random());
      carousel.addAllShapes(shapes);
      game.create();
    });
}

window.onload = () => main();
