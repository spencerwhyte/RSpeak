//
//  LimitedTextView.m
//  IOSAnonQuestion
//
//  Created by Spencer Whyte on 2014-05-11.
//  Copyright (c) 2014 Spencer Whyte. All rights reserved.
//

#import "LimitedTextView.h"

#define MAXIMUM_QUESTION_LENGTH 166

@implementation LimitedTextView

- (id)initWithFrame:(CGRect)frame
{
    self = [super initWithFrame:frame];
    if (self) {
        self.textView = [[UITextView alloc] initWithFrame:CGRectMake(0, 0, 300, 300)];
        self.textView.autoresizingMask = UIViewAutoresizingFlexibleWidth | UIViewAutoresizingFlexibleHeight;
        self.textView.returnKeyType = UIReturnKeyNext;
        self.textView.delegate = self;
        self.characterCountDisplay = [[UILabel alloc] init];
     
        [self addSubview:self.textView];
        [self addSubview: self.characterCountDisplay];
    }
    return self;
}

-(void)viewDidAppear{
    NSLog(@"Got callback");
    //  [self.textView becomeFirstResponder];
}


- (BOOL)textField:(UITextField *)textField shouldChangeCharactersInRange:(NSRange)range replacementString:(NSString *)string
{
    
    return !([textField.text length]> MAXIMUM_QUESTION_LENGTH && [string length] > range.length);
    
}

- (void)textViewDidChange:(UITextView *)textView
{
    CGFloat fixedWidth = textView.frame.size.width;
    CGSize newSize = [textView sizeThatFits:CGSizeMake(fixedWidth, MAXFLOAT)];
    CGRect newFrame = textView.frame;
    newFrame.size = CGSizeMake(fmaxf(newSize.width, fixedWidth), newSize.height);
    textView.frame = newFrame;
}

/*
// Only override drawRect: if you perform custom drawing.
// An empty implementation adversely affects performance during animation.
- (void)drawRect:(CGRect)rect
{
    // Drawing code
}
*/

@end
