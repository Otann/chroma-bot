(ns chroma-bot.images
  (import javax.imageio.ImageIO
          java.awt.Color
          java.awt.image.BufferedImage
          java.io.ByteArrayOutputStream
          (java.io File)))

(defn- make-buffered-image [width heigth [r g b]]
  (let [image   (BufferedImage. width heigth BufferedImage/TYPE_INT_ARGB)
        graphic (.createGraphics image)]
    (.setColor graphic (Color. r g b))
    (.fillRect graphic 0 0 width heigth)
    image))

(defn generate-file [{width   :width
                      heigth  :heigth
                      [r g b] :rgb}]
  (let [^BufferedImage
        image (make-buffered-image width heigth [r g b])
        file  (File/createTempFile "image" "png")]
    (ImageIO/write image "png" file)
    file))

(defn generate-bytearray [{width   :width
                           heigth  :heigth
                           [r g b] :rgb}]
  (let [^BufferedImage
        image (make-buffered-image width heigth [r g b])
        stream (ByteArrayOutputStream.)]
    (ImageIO/write image "png" stream)
    (.toByteArray stream)))

